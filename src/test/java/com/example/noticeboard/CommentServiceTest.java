package com.example.noticeboard;

import com.example.noticeboard.dto.request.ReqCreateCommentDto;
import com.example.noticeboard.dto.request.ReqUpdateCommentDto;
import com.example.noticeboard.dto.response.ResCommentDto;
import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.service.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @Test
    @DisplayName("댓글 작성")
    void addComment() {

        // given
        long postId = 1L;
        ReqCreateCommentDto dto = new ReqCreateCommentDto();
        dto.setUserId(1L);
        dto.setBody("새로운 댓글");

        // when
        List<ResCommentDto> resCommentDtos = commentService.addComment(postId, dto);

        // then
        assertThat(resCommentDtos.get(2)
                                 .getBody()).isEqualTo("새로운 댓글");
        assertThat(resCommentDtos.get(2)
                                 .getChildren()
                                 .size()).isEqualTo(0);
    }

    @Test
    @DisplayName("댓글 작성 실패, 댓글 사용 비활성화 게시글")
    void addCommentFail() {
        // given
        long postId = 2L;
        ReqCreateCommentDto dto = new ReqCreateCommentDto();
        dto.setUserId(1L);
        dto.setBody("새로운 댓글");

        // when
        BaseException exception = Assertions.assertThrows(BaseException.class, () -> commentService.addComment(postId, dto));

        // then
        assertThat(exception.getErrorCode()).isEqualTo("0204");
        assertThat(exception.getMessage()).isEqualTo("댓글 작성이 불가한 게시글입니다.");
    }

    @Test
    @DisplayName("댓글 조회")
    void getComments() {
        // given
        long postId = 1;

        // when
        List<ResCommentDto> comments = commentService.getComments(postId);

        // then
        assertThat(comments.size()).isEqualTo(2);

        ResCommentDto comment1 = comments.get(0);
        assertThat(comment1.getBody()).isEqualTo("댓글1");
        assertThat(comment1.getChildren()
                           .size()).isEqualTo(2);

        ResCommentDto childOfComment1 = comment1.getChildren()
                                                .get(0);
        assertThat(childOfComment1.getBody()).isEqualTo("depth2 댓글1");
        assertThat(childOfComment1.getChildren()
                                  .size()).isEqualTo(1);

        assertThat(childOfComment1.getChildren()
                                  .get(0)
                                  .getBody()).isEqualTo("depth3 댓글1");
    }

    @Test
    @DisplayName("댓글 수정")
    void editComment() {
        // given
        long postId = 1L;
        long commentId = 1L;
        ReqUpdateCommentDto dto = new ReqUpdateCommentDto();
        dto.setUserId(1L);
        dto.setBody("수정된 댓글 내용");

        // when
        List<ResCommentDto> resCommentDtos = commentService.updateComment(postId, commentId, dto);

        // then
        assertThat(resCommentDtos.get(0)
                                 .getBody()).isEqualTo("수정된 댓글 내용");
    }
}
