package com.sparta.newsfeed.exception;

public class PostNotFoundException extends RuntimeException {
    //게시물이 존재하지 않을 때 발생하는 예외
    public PostNotFoundException(String message) {
        super(message);
    }
}
