package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.dto.UpdatePasswordRequestDto;
import com.sparta.newsfeed.dto.user.LoginRequestDto;
import com.sparta.newsfeed.dto.user.SignupRequestDto;
import com.sparta.newsfeed.dto.user.UserResponseDto;
import com.sparta.newsfeed.dto.user.WithdrawRequestDto;
import com.sparta.newsfeed.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignupRequestDto req) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userService.signup(req));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto req, HttpServletResponse res) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.login(req, res));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/my/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequestDto wreq, HttpServletRequest hreq) {
        try {
            userService.withdraw(wreq, hreq);
            return ResponseEntity.ok("withdraw-success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("withdraw-failed");
        }
    }

    @GetMapping("/my/posts")
    public ResponseEntity<PostResponseDto> getMyPosts(HttpServletRequest req) {

        userService.getMyPosts(req);
        return null;
    }

    @PutMapping("/my/updatePassword")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequestDto ureq, HttpServletRequest hreq) {
        try {
            userService.updatePassword(ureq, hreq);
            return ResponseEntity.ok("update-password-success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("update-password-failed");
        }
    }
}
