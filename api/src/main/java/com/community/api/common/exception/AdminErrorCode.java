package com.community.api.common.exception;

import com.community.api.common.exception.inteface.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AdminErrorCode implements ErrorCode {
    ALREADY_EXIST_IP(HttpStatus.BAD_REQUEST, "이미 등록된 IP 입니다"),
    NO_EXIST_IP(HttpStatus.BAD_REQUEST, "존재하지 않는 IP 입니다"),
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
