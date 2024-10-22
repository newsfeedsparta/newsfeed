package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.profile.ProfileRequestDto;
import com.sparta.newsfeed.dto.profile.ProfileResponseDto;
import com.sparta.newsfeed.entity.Profile;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.ProfileRepository;
import com.sparta.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    //create
    public ProfileResponseDto createProfile(ProfileRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow();  //userId를 이용하여 userRepository에서 유저를 조회
        Profile profile = profileRepository.save(Profile.from(requestDto, user));  //ProfileRequestDto와 User 객체를 사용하여 새로운 Profile 객체를 생성
        user.setProfile(profile);
        return new ProfileResponseDto(profile);
    }

    //read
    public ProfileResponseDto getProfile(Long id) {
        Profile profile = profileRepository.findById(id)  // profileRepository에서 해당 프로필을 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 존재하지 않습니다."));
        return new ProfileResponseDto(profile);
    }

    //updaate
    public ProfileResponseDto updateProfile(Long id, ProfileRequestDto requestDto) {
        Profile profile = profileRepository.findById(id)  // profileRepository에서 해당 프로필을 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 존재하지 않습니다."));

        User user = userRepository.findById(requestDto.getUserId())   // ProfileRequestDto로부터 전달받은 userId를 사용하여 userRepository에서 해당 User 객체를 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        profile.initData(requestDto, user); //조회한 Profile 객체와 User 객체를 사용하여 프로필 데이터를 업데이트
        Profile updatedProfile = profileRepository.save(profile);  //수정된 프로필 정보를 profileRepository를 통해 데이터베이스에 저장
        return new ProfileResponseDto(updatedProfile);
    }

}
