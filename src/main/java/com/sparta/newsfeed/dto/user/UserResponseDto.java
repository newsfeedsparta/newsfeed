package com.sparta.newsfeed.dto.user;

import com.sparta.newsfeed.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private String user;

    public UserResponseDto(User user) {
        this.user = user.getUsername();
    }
}
