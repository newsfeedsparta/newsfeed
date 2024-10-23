package com.sparta.newsfeed.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyFriendResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String selfIntroduction;
}
