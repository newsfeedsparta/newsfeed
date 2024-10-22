package com.sparta.newsfeed.dto.profile;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private Long userId;
    private String username;
    private String selfIntroduction;

}
