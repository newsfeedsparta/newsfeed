package com.sparta.newsfeed.exception;

public class FriendRequestNotFoundException extends RuntimeException {

    // 기본 생성자
    public FriendRequestNotFoundException() {
        super("Friend request not found");
    }

    // 사용자 정의 메시지를 받을 수 있는 생성자
    public FriendRequestNotFoundException(String message) {
        super(message);
    }

    // 예외 발생 시 원인을 포함할 수 있는 생성자
    public FriendRequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
