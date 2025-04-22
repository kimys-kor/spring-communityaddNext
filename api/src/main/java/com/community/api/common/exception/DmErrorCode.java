package com.community.api.common.exception;

import com.community.api.common.exception.inteface.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DmErrorCode implements ErrorCode {
    No_EXIST_DM(HttpStatus.BAD_REQUEST, "존재하지 않는 메세지 ID입니다"),
    DM_CANNOT_SEND_TO_SELF(HttpStatus.BAD_REQUEST, "메세지는 자기 자신에게 보낼 수 없습니다."),
    DM_ONLY_READ_RECEIVER(HttpStatus.BAD_REQUEST, "메세지는 자기 자신만 읽을 수 있습니다."),
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
