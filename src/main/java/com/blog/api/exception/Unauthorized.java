package com.blog.api.exception;

/**
 * status -> 404
 */
public class Unauthorized extends MyException{

    private static final String MESSAGE = "인증이 필요";



    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
