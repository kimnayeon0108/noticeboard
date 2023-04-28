package com.example.noticeboard.service;

import com.example.noticeboard.domain.Category;
import com.example.noticeboard.domain.Post;
import com.example.noticeboard.domain.PostFile;
import com.example.noticeboard.domain.User;
import com.example.noticeboard.dto.PagingResponse;
import com.example.noticeboard.dto.PostDto;
import com.example.noticeboard.dto.PostListRequest;
import com.example.noticeboard.dto.PostRequest;
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

    public PostDto addPost(PostRequest postRequest, MultipartFile[] files) throws IOException {
        Category category = categoryRepository.findById(postRequest.getCategoryId()).orElseThrow(() -> new RuntimeException("카테고리 미존재"));
        User user = userRepository.findById(postRequest.getUserId()).orElseThrow(() -> new RuntimeException("유저 미존재"));

        Post post = Post.builder().user(user)
                .title(postRequest.getTitle())
                .body(postRequest.getBody())
                .password(postRequest.getPassword())
                .publicState(postRequest.isPublic())
                .category(category)
                .commentActiveState(postRequest.isCommentActive())
                .build();

        postRepository.save(post);

        List<String> filenames = new ArrayList<>();
        if (files != null) {
            List<PostFile> postFiles = saveAsNewFilename(files, post);
            filenames = postFiles.stream().map(PostFile::getFilename).collect(Collectors.toList());
        }

        return PostDto.of(post, filenames);
    }

    private List<PostFile> saveAsNewFilename(MultipartFile[] files, Post post) throws IOException {
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
        return postFiles;
    }

    @Transactional(readOnly = true)
    public PagingResponse<PostDto> getPosts(PostListRequest postListRequest) {

        Page<PostDto> postDtoPage = postRepository.findAllByConditions(postListRequest,
                        PageRequest.of(postListRequest.getPage() - 1, postListRequest.getPageSize()))
                .map(PostDto::of);

        return PagingResponse.of(postDtoPage);
    }

    @Transactional(readOnly = true)
    public Boolean validatePassword(long postId, PostPasswordRequest postPasswordRequest) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("게시글 미존재"));     //Todo: custom 예외

        if (post.hasPassword() && post.isPasswordEqual(postPasswordRequest.getPassword())) {
            return true;
        }

        if (!post.isPasswordEqual(postPasswordRequest.getPassword())) {
            throw new RuntimeException("유효하지 않은 비밀번호");
        }

        return false;
    }
}
