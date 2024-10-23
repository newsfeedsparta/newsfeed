package com.sparta.newsfeed.dto.profile;

import com.sparta.newsfeed.entity.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import com.sparta.newsfeed.dto.user.UserResponseDto;

@Getter
@AllArgsConstructor
public class ProfileResponseDto {
    private Long id;
    private UserResponseDto user;
    private String selfIntroduction;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
