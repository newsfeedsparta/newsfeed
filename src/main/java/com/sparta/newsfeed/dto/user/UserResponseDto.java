package com.sparta.newsfeed.dto.user;

import com.sparta.newsfeed.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private String username;
    private String email;

    public UserResponseDto(User user) {
        this.username = user.getUsername();
    }
}