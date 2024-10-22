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

    public ProfileResponseDto(Profile profile) {
        this.id = profile.getId();
        this.user = new UserResponseDto(profile.getUser());  //유저리스폰스 디티오에 있는 겟 유저네임을 가져와서 새로 유저리스폰스디티오에 넣어서 user에 넣음
        this.selfIntroduction = profile.getSelfIntroduction();
        this.createdAt = profile.getCreatedAt();
        this.modifiedAt = profile.getModifiedAt();
    }

}
