package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.profile.ProfileRequestDto;
import com.sparta.newsfeed.dto.profile.ProfileResponseDto;
import com.sparta.newsfeed.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;  //ProfileService 객체를 주입

    // 프로필 작성
    @PostMapping()
    public ResponseEntity<ProfileResponseDto> createProfile(@RequestBody ProfileRequestDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(profileService.createProfile(requestDto));
    }

    // 내 프로필 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> getProfile(@PathVariable Long id) {
        ProfileResponseDto responseDto = profileService.getProfile(id);
        return ResponseEntity.ok(responseDto);
    }

    // 프로필 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponseDto> updateProfile(@PathVariable Long id, @RequestBody ProfileRequestDto requestDto) {
        ProfileResponseDto responseDto = profileService.updateProfile(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}
