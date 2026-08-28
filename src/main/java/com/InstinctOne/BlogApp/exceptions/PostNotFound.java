package com.InstinctOne.BlogApp.exceptions;

public class PostNotFound extends RuntimeException {
    public PostNotFound(String message) {
        super(message);
    }
}
