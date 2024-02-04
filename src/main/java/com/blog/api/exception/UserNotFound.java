package com.blog.api.exception;

/**
 * status -> 404
 */
public class UserNotFound extends MyException {

	private static final String MESSAGE = "존재하지 않는 사용자입니다.";

	public UserNotFound() {
		super(MESSAGE);
	}

	public UserNotFound(Throwable cause) {
		super(MESSAGE, cause);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
