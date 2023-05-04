package com.example.noticeboard.service;

import com.example.noticeboard.domain.Category;
import com.example.noticeboard.domain.Post;
import com.example.noticeboard.domain.PostFile;
import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.request.*;
import com.example.noticeboard.dto.response.ResPagingDto;
import com.example.noticeboard.dto.response.ResPostDto;
import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final PostFileRepository postFileRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ResPostDto addPost(ReqCreatePostDto reqCreatePostDto) throws IOException {
        Category category = categoryRepository.findById(reqCreatePostDto.getCategoryId()).orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));
        User user = userRepository.findById(reqCreatePostDto.getUserId()).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.builder().user(user)
                .title(reqCreatePostDto.getTitle())
                .body(reqCreatePostDto.getBody())
                .password(reqCreatePostDto.getPassword())
                .publicState(reqCreatePostDto.getIsPublic())
                .category(category)
                .commentActiveState(reqCreatePostDto.getIsCommentActive())
                .build();

        postRepository.save(post);

        List<String> filenames = new ArrayList<>();
        if (reqCreatePostDto.getMultipartFiles() != null) {
            filenames = saveFiles(reqCreatePostDto.getMultipartFiles(), post);
        }

        return ResPostDto.of(post, filenames);
    }

    private List<String> saveFiles(MultipartFile[] files, Post post) throws IOException {
        if (files.length > 3) {
            throw new BaseException(ErrorCode.POST_FILE_EXCEEDED);
        }

        List<PostFile> postFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            File renamedFile = new File(randomUUID() + "_" + file.getOriginalFilename());
            file.transferTo(renamedFile);

            PostFile postFile = new PostFile(post, renamedFile.getName(), file.getContentType());
            postFiles.add(postFile);
        }
        postFileRepository.saveAll(postFiles);

        return postFiles.stream().map(PostFile::getFilename).collect(Collectors.toList());
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));

        if (post.hasPassword() && post.isPasswordEqual(reqValidatePostPasswordDto.getPassword())) {
            return true;
        }

        if (!post.isPasswordEqual(reqValidatePostPasswordDto.getPassword())) {
            throw new BaseException(ErrorCode.INVALID_POST_PASSWORD);
        }

        return false;
    }

    public ResPostDto getPost(long postId) {
        postRepository.updateViewCount(postId);

        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));

        return ResPostDto.of(post);
    }

    public ResPostDto updatePost(long postId, ReqUpdatePostDto reqUpdatePostDto) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(reqUpdatePostDto.getUserId()).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        if (!user.isWriter(post.getUser().getId())) {
            throw new BaseException(ErrorCode.USER_NOT_ALLOWED);
        }

        Category category = categoryRepository.findById(reqUpdatePostDto.getCategoryId()).orElseThrow(() -> new BaseException(ErrorCode.CATEGORY_NOT_FOUND));
        post.update(reqUpdatePostDto, category);

        // 기존 게시글 파일 삭제
        postFileRepository.deleteAllByPostId(postId);

        List<String> filenames = new ArrayList<>();
        if (reqUpdatePostDto.getMultipartFiles() != null) {
            filenames = saveFiles(reqUpdatePostDto.getMultipartFiles(), post);
        }

        return ResPostDto.of(post, filenames);
    }

    public void deletePost(ReqDeletePostDto reqDeletePostDto) {
        List<Post> posts = postRepository.findAllByIdIn(reqDeletePostDto.getPostIds());
        User user = userRepository.findById(reqDeletePostDto.getUserId()).orElseThrow(() -> new BaseException(ErrorCode.USER_NOT_FOUND));

        posts.forEach(post -> {
            if (!user.isWriter(post.getUser().getId())) {
                throw new BaseException(ErrorCode.USER_NOT_ALLOWED);
            }
            postFileRepository.deleteAllByPostId(post.getId());

            commentRepository.deleteAllByPostId(post.getId());
        });

        postRepository.deleteAllById(reqDeletePostDto.getPostIds());
    }
}
