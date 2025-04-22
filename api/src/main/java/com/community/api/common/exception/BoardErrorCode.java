package com.community.api.common.exception;

import com.community.api.common.exception.inteface.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러"),
    POST_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 게시글입니다."),
    PARENT_NOT_EXIST(HttpStatus.BAD_REQUEST, "대댓글을 작성할 댓글이 존재하지 않습니다"),
    POST_WRITER_NOT_EQUALS(HttpStatus.BAD_REQUEST, "게시글 작성자가 아닙니다."),
    COMMENT_ONLY_CAN_2STEP(HttpStatus.BAD_REQUEST, "대댓글 까지만 허용됩니다."),
    BAD_COMMENT_WRITE_REQUEST(HttpStatus.BAD_REQUEST, "올바르지 않은 대댓글 작성루트입니다"),
    POINT_NOT_ENOUGH(HttpStatus.NOT_ACCEPTABLE, "포인트가 부족합니다"),
    ;

    public final HttpStatus status;
    public final String message;

    @Override
    public HttpStatus defaultHttpStatus() {
        return status;
    }

    @Override
    public String defaultMessage() {
        return message;
    }

    @Override
    public CommonException defaultException() {
        return new CommonException(this);
    }

    @Override
    public CommonException defaultException(Throwable cause) {
        return new CommonException(this, cause);
    }
}
