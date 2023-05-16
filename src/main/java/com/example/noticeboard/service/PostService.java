package com.example.noticeboard.service;

import com.example.noticeboard.domain.*;
import com.example.noticeboard.dto.request.*;
import com.example.noticeboard.dto.response.ResPagingDto;
import com.example.noticeboard.dto.response.ResPostDto;
import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.repository.*;
import com.example.noticeboard.security.dto.UserDetailsDto;
import com.example.noticeboard.type.CategoryDepthType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final UserService userService;

    public ResPostDto addPost(ReqCreatePostDto reqCreatePostDto, MultipartFile[] multipartFiles,
                              UserDetailsDto userDetails) throws IOException {
        Category category = categoryRepository.findById(reqCreatePostDto.getCategoryId())
                                              .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));

        User user = userService.getUser(userDetails.getUsername());

        Post post = Post.builder()
                        .user(user)
                        .title(reqCreatePostDto.getTitle())
                        .body(reqCreatePostDto.getBody())
                        .password(reqCreatePostDto.getPassword())
                        .publicState(reqCreatePostDto.getIsPublic())
                        .commentActiveState(reqCreatePostDto.getIsCommentActive())
                        .build();

        postRepository.save(post);

        List<String> filenames = new ArrayList<>();
        if (!ObjectUtils.isEmpty(multipartFiles)) {
            filenames = saveFiles(multipartFiles, post);
        }

        List<PostCategory> postCategories = saveAllDepthPostCategories(category, post);

        Map<String, String> allDepthCategoryNames = getAllDepthCategoryNames(postCategories);

        return ResPostDto.of(post, filenames, allDepthCategoryNames);
    }

    private Map<String, String> getAllDepthCategoryNames(List<PostCategory> postCategories) {
        Map<String, String> allDepthCategoryNames = new HashMap<>();

        postCategories.forEach(postCategory -> {
            Category category = postCategory.getCategory();
            allDepthCategoryNames.put(CategoryDepthType.getFromDepth(category.getDepth())
                                                       .getName(), category.getName());
        });

        return allDepthCategoryNames;
    }

    private List<String> saveFiles(MultipartFile[] files, Post post) throws IOException {
        List<PostFile> postFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            File renamedFile = new File(randomUUID() + "_" + file.getOriginalFilename());
            file.transferTo(renamedFile);

            PostFile postFile = new PostFile(post, renamedFile.getName(), file.getContentType());
            postFiles.add(postFile);
        }
        postFileRepository.saveAll(postFiles);

        return postFiles.stream()
                        .map(PostFile::getFilename)
                        .collect(Collectors.toList());
    }

    private List<PostCategory> saveAllDepthPostCategories(Category lastDepthCategory, Post post) {
        List<PostCategory> postCategories = new ArrayList<>();

        Category category = lastDepthCategory;

        for (int i = 0; i < lastDepthCategory.getDepth(); i++) {
            PostCategory postCategory = new PostCategory(post, category);
            postCategories.add(postCategory);

            category = category.getParentCategory();
        }
        postCategoryRepository.saveAll(postCategories);

        return postCategories;
    }

    @Transactional(readOnly = true)
    public ResPagingDto<ResPostDto> getPosts(ReqPostListParamsDto reqPostListParamsDto) {
        Page<ResPostDto> postDtoPage = postRepository.findAllByConditions(reqPostListParamsDto,
                                                             PageRequest.of(reqPostListParamsDto.getPage() - 1, reqPostListParamsDto.getPageSize()))
                                                     .map(ResPostDto::of);

        return ResPagingDto.of(postDtoPage);
    }

    @Transactional(readOnly = true)
    public boolean validatePassword(long postId, ReqValidatePostPasswordDto reqValidatePostPasswordDto) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));

        if (!post.hasPassword() || !post.isPasswordEqual(reqValidatePostPasswordDto.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_POST_PASSWORD);
        }

        return post.isPasswordEqual(reqValidatePostPasswordDto.getPassword());
    }

    public ResPostDto getPost(long postId) {
        postRepository.updateViewCount(postId);

        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));

        Map<String, String> allDepthCategoryNames = getAllDepthCategoryNames(post.getPostCategories());

        List<String> postFilenames = post.getPostFiles()
                                         .stream()
                                         .filter(postFile -> !postFile.isDeleted())
                                         .map(PostFile::getFilename)
                                         .collect(Collectors.toList());

        return ResPostDto.of(post, postFilenames, allDepthCategoryNames);
    }

    public ResPostDto updatePost(long postId, ReqUpdatePostDto reqUpdatePostDto, MultipartFile[] multipartFiles,
                                 UserDetailsDto userDetails) throws IOException {

        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        User user = userService.getUser(userDetails.getUsername());

        if (!user.isWriter(post.getUser()
                               .getId())) {
            throw new BaseException(ErrorCode.USER_NOT_ALLOWED);
        }

        post.update(reqUpdatePostDto);

        // 기존 게시글 파일 삭제
        postFileRepository.deleteAllByPostIdIn(List.of(postId));

        List<String> filenames = new ArrayList<>();
        if (multipartFiles != null) {
            filenames = saveFiles(multipartFiles, post);
        }

        long lastDepthCategoryId = categoryRepository.findLastDepthCategoryIdByPostId(post.getId());

        // 게시글의 최하위 카테고리가 변경하려는 카테고리와 다르면 post_category 삭제하고 새로 insert
        List<PostCategory> postCategories = updatePostCategories(reqUpdatePostDto.getCategoryId(), post, lastDepthCategoryId);

        Map<String, String> allDepthCategoryNames = getAllDepthCategoryNames(postCategories);

        return ResPostDto.of(post, filenames, allDepthCategoryNames);
    }

    private List<PostCategory> updatePostCategories(long updatedCategoryId, Post post, long lastDepthCategoryId) {

        if (lastDepthCategoryId != updatedCategoryId) {
            Category updatedCategory = categoryRepository.findById(updatedCategoryId)
                                                         .orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));

            postCategoryRepository.deleteAllByPostIdInBatch(List.of(post.getId()));

            return saveAllDepthPostCategories(updatedCategory, post);
        }

        return post.getPostCategories();
    }

    public void deletePost(ReqDeletePostDto reqDeletePostDto, UserDetailsDto userDetails) {
        List<Long> postIds = new ArrayList<>(reqDeletePostDto.getPostIds());
        List<Post> posts = postRepository.findAllByIdIn(postIds);

        if (postIds.size() != posts.size()) {
            throw new BaseException(ErrorCode.POST_NOT_FOUND);
        }

        User user = userService.getUser(userDetails.getUsername());

        posts.forEach(post -> {
            if (!user.isWriter(post.getUser()
                                   .getId())) {
                throw new BaseException(ErrorCode.USER_NOT_ALLOWED);
            }
        });

        postFileRepository.deleteAllByPostIdIn(postIds);
        commentRepository.deleteAllByPostIdIn(postIds);
        postRepository.deleteAllByIdIn(postIds);
        postCategoryRepository.deleteAllByPostIdInBatch(postIds);
    }
}
