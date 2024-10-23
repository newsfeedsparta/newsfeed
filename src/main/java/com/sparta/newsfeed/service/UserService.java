package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.UpdatePasswordRequestDto;
import com.sparta.newsfeed.dto.user.LoginRequestDto;
import com.sparta.newsfeed.dto.user.SignupRequestDto;
import com.sparta.newsfeed.dto.user.UserResponseDto;
import com.sparta.newsfeed.dto.user.WithdrawRequestDto;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;
import com.sparta.newsfeed.util.JwtUtil;
import com.sparta.newsfeed.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public UserResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String passwordCheck = requestDto.getPasswordCheck();

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        if (!PasswordEncoder.verifyPassword(password)) {
            throw new IllegalArgumentException("비밀번호의 형식이 올바르지 않습니다.");
        }

        // 비밀번호 확인
        if (!passwordCheck.equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        // 사용자 등록
        User user = new User(username, encodedPassword, email);
        User savedUser = userRepository.save(user);

        return savedUser.to();
    }

    @Transactional
    public UserResponseDto login(LoginRequestDto requestDto, HttpServletResponse res) {
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(user.getEmail());
        jwtUtil.addJwtToCookie(token, res);

        return user.to();
    }

    @Transactional
    public void withdraw(WithdrawRequestDto wreq, HttpServletRequest hreq) {
        // hreq에 담긴 유저가 존재하는지 확인
        User user = (User) hreq.getAttribute("user");

        // 유저의 비밀번호와 wreq의 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(wreq.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.delete(user);
    }

    public void getMyPosts(HttpServletRequest req) {

    }

    public void updatePassword(UpdatePasswordRequestDto ureq, HttpServletRequest hreq) {
        User user = (User) hreq.getAttribute("user");

        if (!passwordEncoder.matches(ureq.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (ureq.getOldPassword().equals(ureq.getNewPassword())) {
            throw new IllegalArgumentException("바꾸고자 하는 비밀번호가 현재 비밀번호와 동일합니다.");
        }

        if (!PasswordEncoder.verifyPassword(ureq.getNewPassword())) {
            throw new IllegalArgumentException("비밀번호의 형식이 올바르지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(ureq.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
