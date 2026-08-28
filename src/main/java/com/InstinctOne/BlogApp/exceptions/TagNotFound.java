package com.InstinctOne.BlogApp.exceptions;

public class TagNotFound extends RuntimeException {
    public TagNotFound(String message) {
        super(message);
    }
}
