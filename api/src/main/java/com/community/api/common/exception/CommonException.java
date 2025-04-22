package com.community.api.common.exception;

import com.community.api.common.exception.inteface.CustomException;
import com.community.api.common.exception.inteface.ErrorCode;

public class CommonException extends CustomException {
	public CommonException() {
		super();
	}
	
	public CommonException(String message) {
		super(message);
	}
	
	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CommonException(ErrorCode errorCode) {
		super(errorCode);
	}
	
	public CommonException(ErrorCode errorCode, Throwable cause) {
		super(errorCode, cause);
	}
}