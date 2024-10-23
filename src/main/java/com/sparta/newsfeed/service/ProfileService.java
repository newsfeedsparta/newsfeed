package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.profile.CreateProfileRequestDto;
import com.sparta.newsfeed.dto.profile.ProfileResponseDto;
import com.sparta.newsfeed.dto.profile.UpdateProfileRequestDto;
import com.sparta.newsfeed.entity.Profile;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.ProfileRepository;
import com.sparta.newsfeed.repository.UserRepository;
import com.sparta.newsfeed.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //create
    public ProfileResponseDto createProfile(CreateProfileRequestDto requestDto, HttpServletRequest hreq) {
        User user = findUserByToken(hreq);

        Profile profile = profileRepository.save(Profile.from(requestDto, user));
        user.setProfile(profile);
        return profile.to();
    }

    //read
    public ProfileResponseDto getProfile(Long id) {
        Profile profile = profileRepository.findById(id)  // profileRepository에서 해당 프로필을 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 존재하지 않습니다."));
        return profile.to();
    }

    public ProfileResponseDto getMyProfile(HttpServletRequest req) {
        User user = findUserByToken(req);

        Profile profile = profileRepository.findByUserId(user.getId()).orElseThrow(
                () -> new IllegalArgumentException("내 프로필이 존재하지 않습니다.")
        );

        return profile.to();
    }

    //update
    public ProfileResponseDto updateProfile(UpdateProfileRequestDto ureq, HttpServletRequest hreq) {
        User user = findUserByToken(hreq);

        Profile profile = profileRepository.findByUserId(user.getId())  // profileRepository에서 해당 프로필을 조회
                .orElseThrow(() -> new IllegalArgumentException("해당 프로필이 존재하지 않습니다."));

        // 비밀번호 일치 확인
        if (!passwordEncoder.matches(ureq.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        profile.update(ureq); //조회한 Profile 객체와 User 객체를 사용하여 프로필 데이터를 업데이트
        Profile updatedProfile = profileRepository.save(profile);  //수정된 프로필 정보를 profileRepository를 통해 데이터베이스에 저장
        return updatedProfile.to();
    }

    private User findUserByToken(HttpServletRequest req) {
        User tempUser = (User) req.getAttribute("user");

        User user = userRepository.findById(tempUser.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

        return user;
    }
}
