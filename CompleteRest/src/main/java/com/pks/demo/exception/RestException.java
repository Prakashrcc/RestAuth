package com.pks.demo.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

public class RestException {
	private final LocalDateTime localDateTime;
	private final HttpStatus httpStatus;
	private final String message;

	public RestException(String message, HttpStatus httpStatus, LocalDateTime localDateTime) {
		super();
		this.message = message;

		this.httpStatus = httpStatus;
		this.localDateTime = localDateTime;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

}
