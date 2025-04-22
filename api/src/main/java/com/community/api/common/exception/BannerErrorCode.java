package com.community.api.common.exception;

import com.community.api.common.exception.inteface.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BannerErrorCode implements ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러"),
    BANNER_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 배너입니다."),
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
