package com.blog.api.exception;

/**
 * status -> 404
 */
public class CommentNotFound extends MyException {

	private static final String MESSAGE = "존재하지 않는 댓글입니다.";

	public CommentNotFound() {
		super(MESSAGE);
	}

	public CommentNotFound(Throwable cause) {
		super(MESSAGE, cause);
	}

	@Override
	public int getStatusCode() {
		return 404;
	}
}
