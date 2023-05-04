package com.example.noticeboard.exception;

public enum ErrorCode {

    // user
    USER_NOT_FOUND("0101", "회원이 존재하지 않습니다."),
    USER_NOT_ALLOWED("0102", "유저의 리소스가 아닙니다."),

    // post
    POST_NOT_FOUND("0201", "게시글이 존재하지 않습니다."),
    POST_FILE_EXCEEDED("0202", "파일 업로드는 3개까지 가능합니다."),
    INVALID_POST_PASSWORD("0203", "게시글 비밀번호가 일치하지 않습니다."),
    INACTIVE_COMMENT_POST("0204", "댓글 작성이 불가한 게시글입니다."),
    POST_NOT_MATCH("0205", "게시글이 일치하지 않습니다."),

    // comment
    COMMENT_NOT_FOUND("0301", "댓글이 존재하지 않습니다."),
    UNDELETABLE_COMMENT("0302", "댓글을 삭제할 수 없습니다."),

    // category
    CATEGORY_NOT_FOUND("0401", "카테고리가 존재하지 않습니다.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return message;
    }
}
