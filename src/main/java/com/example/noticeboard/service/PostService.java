package com.example.noticeboard.service;

import com.example.noticeboard.domain.Category;
import com.example.noticeboard.domain.Post;
import com.example.noticeboard.domain.PostFile;
import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.*;
import com.example.noticeboard.repository.CategoryRepository;
import com.example.noticeboard.repository.PostFileRepository;
import com.example.noticeboard.repository.PostRepository;
import com.example.noticeboard.repository.UserRepository;
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

    public ResPostDto addPost(ReqCreatePostDto reqCreatePostDto, MultipartFile[] files) throws IOException {
        Category category = categoryRepository.findById(reqCreatePostDto.getCategoryId()).orElseThrow(() -> new RuntimeException("카테고리 미존재"));
        User user = userRepository.findById(reqCreatePostDto.getUserId()).orElseThrow(() -> new RuntimeException("유저 미존재"));

        Post post = Post.builder().user(user)
                .title(reqCreatePostDto.getTitle())
                .body(reqCreatePostDto.getBody())
                .password(reqCreatePostDto.getPassword())
                .publicState(reqCreatePostDto.isPublic())
                .category(category)
                .commentActiveState(reqCreatePostDto.isCommentActive())
                .build();

        postRepository.save(post);

        List<String> filenames = new ArrayList<>();
        if (files != null) {
            filenames = saveFiles(files, post);
        }

        return ResPostDto.of(post, filenames);
    }

    private List<String> saveFiles(MultipartFile[] files, Post post) throws IOException {
        if (files.length > 3) {
            throw new RuntimeException("파일은 최대 3개까지 업로드 가능");       //Todo: custom exception
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
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));     //Todo: custom 예외

        if (post.hasPassword() && post.isPasswordEqual(reqValidatePostPasswordDto.getPassword())) {
            return true;
        }

        if (!post.isPasswordEqual(reqValidatePostPasswordDto.getPassword())) {
            throw new RuntimeException("유효하지 않은 비밀번호");
        }

        return false;
    }

    public ResPostDto getPost(long postId) {
        postRepository.updateViewCount(postId);

        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));     //Todo: custom 예외

        return ResPostDto.of(post);
    }

    public ResPostDto updatePost(long postId, ReqUpdatePostDto reqUpdatePostDto, MultipartFile[] files) throws IOException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));     //Todo: custom 예외
        User user = userRepository.findById(reqUpdatePostDto.getUserId()).orElseThrow(() -> new RuntimeException("유저 미존재"));

        if (!user.isWriter(post.getUser().getId())) {
            throw new RuntimeException("본인의 게시글만 수정 가능");
        }

        Category category = categoryRepository.findById(reqUpdatePostDto.getCategoryId()).orElseThrow(() -> new RuntimeException("카테고리 미존재"));
        post.update(reqUpdatePostDto, category);

        // 기존 게시글 파일 삭제
        List<PostFile> postFiles = postFileRepository.findAllByPostId(postId);
        postFiles.forEach(PostFile::delete);

        List<String> filenames = new ArrayList<>();
        if (files != null) {
            filenames = saveFiles(files, post);
        }

        return ResPostDto.of(post, filenames);
    }
}
