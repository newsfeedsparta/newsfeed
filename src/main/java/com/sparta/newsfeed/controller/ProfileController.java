package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.ErrorResponseDto;
import com.sparta.newsfeed.dto.profile.CreateProfileRequestDto;
import com.sparta.newsfeed.dto.profile.ProfileResponseDto;
import com.sparta.newsfeed.dto.profile.UpdateProfileRequestDto;
import com.sparta.newsfeed.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Object> createProfile(@RequestBody CreateProfileRequestDto creq, HttpServletRequest hreq) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(profileService.createProfile(creq, hreq));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 내 프로필 조회
    @GetMapping()
    public ResponseEntity<Object> getMyProfile(HttpServletRequest req) {
        try {
            ProfileResponseDto responseDto = profileService.getMyProfile(req);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // id로 프로필 조회
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProfile(@PathVariable Long id) {
        try {
            ProfileResponseDto responseDto = profileService.getProfile(id);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }
    // 프로필 수정
    @PutMapping()
    public ResponseEntity<Object> updateProfile(@RequestBody UpdateProfileRequestDto ureq, HttpServletRequest hreq) {
        try {
            ProfileResponseDto responseDto = profileService.updateProfile(ureq, hreq);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }
}
