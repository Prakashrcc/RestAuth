package com.pks.demo.exception;

public class RestRequestException extends RuntimeException {

	public RestRequestException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public RestRequestException(String message) {
		super(message);
		
	}

	
}
