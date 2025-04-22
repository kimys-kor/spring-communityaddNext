package com.community.api.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response<T> extends ResponseEntity<ResponseBody<T>> {
    public Response(HttpStatus status, ResultCode resultCode, T data) {
        super(new ResponseBody<>(status, resultCode.getStatusCode(), resultCode.getStatusMessage(), data), status);
    }

    public Response(ResultCode resultCode) {
        this(resultCode.getStatus(), resultCode, null);
    }

    public Response(T data) {
        this(HttpStatus.OK, ResultCode.DATA_NORMAL_PROCESSING, data);
    }

    public Response(ResultCode resultCode, T data) {
        this(resultCode.getStatus(), resultCode, data);
    }
}