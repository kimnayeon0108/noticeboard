package com.example.noticeboard;

import com.example.noticeboard.dto.request.ReqCreatePostDto;
import com.example.noticeboard.dto.request.ReqPostListParamsDto;
import com.example.noticeboard.dto.request.ReqUpdatePostDto;
import com.example.noticeboard.dto.request.ReqValidatePostPasswordDto;
import com.example.noticeboard.dto.response.ResPostDto;
import com.example.noticeboard.service.PostService;
import com.example.noticeboard.type.PostOrderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    PostService postService;

    @Test
    @DisplayName("게시글 작성")
    void addPost() throws IOException {
        // given
        ReqCreatePostDto reqCreatePostDto = new ReqCreatePostDto();
        reqCreatePostDto.setUserId(1L);
        reqCreatePostDto.setTitle("제목");
        reqCreatePostDto.setBody("내용");
        reqCreatePostDto.setCategoryId(3L);
        reqCreatePostDto.setIsPublic(true);
        reqCreatePostDto.setIsCommentActive(true);

        // when
        ResPostDto resPostDto = postService.addPost(reqCreatePostDto);

        // then
        assertThat(resPostDto.getTitle()).isEqualTo("제목");
        assertThat(resPostDto.getBody()).isEqualTo("내용");
        assertThat(resPostDto.getCategoryName()).isEqualTo("소분류");
        assertThat(resPostDto.getWriterName()).isEqualTo("김나연");
        assertThat(resPostDto.getFilenames().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 목록 조회, 작성자 필터링")
    void getPostsByWriterName() {
        // given
        ReqPostListParamsDto dto = new ReqPostListParamsDto();
        dto.setWriterName("김나연");
        dto.setPage(1);
        dto.setPageSize(10);
        dto.setOrderBy(PostOrderType.CREATED_AT);

        // when
        List<ResPostDto> posts = postService.getPosts(dto).getContents();

        // then
        assertThat(posts.size()).isEqualTo(4);
        assertThat(posts.get(0).getTitle()).isEqualTo("게시글4");
        assertThat(posts.get(0).getFilenames().size()).isEqualTo(1);
        assertThat(posts.get(1).getTitle()).isEqualTo("게시글3");
        assertThat(posts.get(2).getTitle()).isEqualTo("게시글2");
        assertThat(posts.get(3).getTitle()).isEqualTo("게시글1");
    }

    @Test
    @DisplayName("게시글 목록 조회, 카테고리 필터링, 조회 수 정렬")
    void getPostsByCategoryName() {
        ReqPostListParamsDto dto = new ReqPostListParamsDto();
        dto.setCategoryName("소분류");
        dto.setPage(1);
        dto.setPageSize(10);
        dto.setOrderBy(PostOrderType.VIEW_COUNT);

        // when
        List<ResPostDto> posts = postService.getPosts(dto).getContents();

        // then
        assertThat(posts.size()).isEqualTo(3);
        assertThat(posts.get(0).getTitle()).isEqualTo("게시글2");
        assertThat(posts.get(1).getTitle()).isEqualTo("게시글3");
        assertThat(posts.get(2).getTitle()).isEqualTo("게시글4");
    }

    @Test
    @DisplayName("게시글 목록 조회, 제목 검색")
    void getPostsByTitle() {
        ReqPostListParamsDto dto = new ReqPostListParamsDto();
        dto.setTitle("1");
        dto.setPage(1);
        dto.setPageSize(10);
        dto.setOrderBy(PostOrderType.CREATED_AT);

        // when
        List<ResPostDto> posts = postService.getPosts(dto).getContents();

        // then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("게시글1");
    }

    @Test
    @DisplayName("게시글 목록 조회, 내용 검색")
    void getPostsByBody() {
        ReqPostListParamsDto dto = new ReqPostListParamsDto();
        dto.setBody("내용");
        dto.setPage(1);
        dto.setPageSize(10);
        dto.setOrderBy(PostOrderType.CREATED_AT);

        // when
        List<ResPostDto> posts = postService.getPosts(dto).getContents();

        // then
        assertThat(posts.size()).isEqualTo(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("게시글1");
    }

    @Test
    @DisplayName("게시글 상세 조회")
    void getPostDetail() {
        // given
        long postId = 1L;

        // when
        ResPostDto post = postService.getPost(postId);

        // then
        assertThat(post.getTitle()).isEqualTo("게시글1");
        assertThat(post.getBody()).isEqualTo("본문 내용");
        assertThat(post.getViewCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 수정")
    void editPosts() throws IOException {
        // given
        long postId = 1L;
        ReqUpdatePostDto updatePostDto = new ReqUpdatePostDto();
        updatePostDto.setUserId(1);
        updatePostDto.setCategoryId(4L);
        updatePostDto.setTitle("수정된 제목");
        updatePostDto.setBody("수정된 본문");
        updatePostDto.setIsPublic(false);

        // when
        ResPostDto resPostDto = postService.updatePost(postId, updatePostDto);

        // then
        assertThat(resPostDto.getTitle()).isEqualTo("수정된 제목");
        assertThat(resPostDto.getBody()).isEqualTo("수정된 본문");
        assertThat(resPostDto.getCategoryName()).isEqualTo("소분류2");
    }

    @Test
    @DisplayName("비밀번호 검증")
    void validatePassword() {
        // given
        long postId = 1L;
        ReqValidatePostPasswordDto dto = new ReqValidatePostPasswordDto();
        dto.setPassword("1234");

        // when
        boolean validated = postService.validatePassword(postId, dto);

        // then
        assertThat(validated).isTrue();
    }
}
