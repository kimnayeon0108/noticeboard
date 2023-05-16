package com.example.noticeboard.controller;

import com.example.noticeboard.dto.request.*;
import com.example.noticeboard.dto.response.ResPagingDto;
import com.example.noticeboard.dto.response.ResPostDto;
import com.example.noticeboard.dto.response.ResponseDto;
import com.example.noticeboard.exception.BaseException;
import com.example.noticeboard.exception.ErrorCode;
import com.example.noticeboard.security.dto.UserDetailsDto;
import com.example.noticeboard.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Tag(name = "post", description = "게시글 api")
public class PostController {

    private static final int MAX_FILE_UPLOAD_COUNT = 3;

    private final PostService postService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 작성", description = "게시글 작성 api", tags = "post")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostDto> addPost(
            @Valid @RequestPart(value = "post")
            @Parameter(schema = @Schema(type = "string", format = "binary"),
                       description = " &nbsp;json 파일을 업로드해 주세요. <br>&nbsp;example: {<br>" +
                               " &nbsp;\"isPublic\": true,<br>" +
                               " &nbsp;\"password\": \"1234\",<br>" +
                               " &nbsp;\"categoryId\": 3,<br>" +
                               " &nbsp;\"title\": \"제목\",<br>" +
                               " &nbsp;\"body\": \"게시글 내용\",<br>" +
                               " &nbsp;\"isCommentActive\": true<br>" +
                               "}") ReqCreatePostDto reqCreatePostDto,
            @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsDto userDetails) throws IOException {

        if (!ObjectUtils.isEmpty(multipartFiles) && multipartFiles.length > MAX_FILE_UPLOAD_COUNT) {
            throw new BaseException(ErrorCode.POST_FILE_EXCEEDED);
        }

        return ResponseDto.success(postService.addPost(reqCreatePostDto, multipartFiles, userDetails));
    }

    @GetMapping
    @Operation(summary = "게시글 리스트 조회", description = "게시글의 목록 조회 api", tags = "post")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<ResPagingDto<ResPostDto>> showPostList(@Valid @ModelAttribute ReqPostListParamsDto reqPostListParamsDto) {
        return ResponseDto.success(postService.getPosts(reqPostListParamsDto));
    }

    @PostMapping("/{postId}/password/validate")
    @Operation(summary = "게시글 비밀번호 검증", description = "게시글의 비밀번호 검증 api, 작성자가 아닌 유저가 게시글에 접근할 때 호출합니다.", tags = "post")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<Boolean> validatePassword(@Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId,
                                                 @Valid @RequestBody ReqValidatePostPasswordDto reqValidatePostPasswordDto) {
        return ResponseDto.success(postService.validatePassword(postId, reqValidatePostPasswordDto));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "게시글 상세 조회", description = "게시글 상세 조회 api", tags = "post")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto<ResPostDto> showPostDetail(@Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId) {
        return ResponseDto.success(postService.getPost(postId));
    }

    @PutMapping(value = "/{postId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "게시글 수정", description = "게시글 수정 api", tags = "post")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<ResPostDto> editPost(
            @Parameter(in = ParameterIn.PATH, description = "게시글 id") @PathVariable long postId,
            @Valid @RequestPart(value = "post")
            @Parameter(schema = @Schema(type = "string", format = "binary"),
                       description = " &nbsp;json 파일을 업로드해 주세요. <br>&nbsp;example: {<br>" +
                               " &nbsp;\"isPublic\": true,<br>" +
                               " &nbsp;\"password\": \"1234\",<br>" +
                               " &nbsp;\"categoryId\": 3,<br>" +
                               " &nbsp;\"title\": \"제목\",<br>" +
                               " &nbsp;\"body\": \"게시글 내용\" <br>" +
                               "}") ReqUpdatePostDto reqUpdatePostDto,
            @RequestPart(value = "file", required = false) MultipartFile[] multipartFiles,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsDto userDetails) throws IOException {

        if (!ObjectUtils.isEmpty(multipartFiles) && multipartFiles.length > MAX_FILE_UPLOAD_COUNT) {
            throw new BaseException(ErrorCode.POST_FILE_EXCEEDED);
        }

        return ResponseDto.success(postService.updatePost(postId, reqUpdatePostDto, multipartFiles, userDetails));
    }

    @DeleteMapping
    @Operation(summary = "게시글 삭제", description = "게시글 삭제 api", tags = "post")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseDto<Void> deletePost(@Valid @RequestBody ReqDeletePostDto reqDeleteDto,
                                        @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsDto userDetails) {

        postService.deletePost(reqDeleteDto, userDetails);
        return ResponseDto.success(null);
    }
}
