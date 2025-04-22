package com.community.api.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBody<T> {
    private HttpStatus status;

    private ResultCode resultCode;

    private String code;
    private String message;

    private T data;

    public ResponseBody(HttpStatus status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }


}

