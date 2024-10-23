package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.ErrorResponseDto;
import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.dto.friend.FriendRequestDto;
import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.dto.friend.MyFriendResponseDto;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.service.FriendService;
import com.sparta.newsfeed.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {
    private FriendService friendService; // 친구 서비스 주입

    // 친구 요청 생성 API
    @PostMapping()
    public ResponseEntity<Object> createFriendRequest(
            @RequestBody FriendRequestDto friendRequestDto) {
        try {
            FriendResponseDto response = friendService.createFriendRequest(friendRequestDto);
            return ResponseEntity.ok(response); // 요청 성공시 응답 반환
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 친구 요청 상태 변경 API
    @PutMapping()
    public ResponseEntity<Object> updateFriendRequest(@PathVariable Long requestId, @RequestParam String status) {
        try {
            FriendResponseDto updatedRequest = friendService.updateFriendRequestStatus(requestId, status);
            return ResponseEntity.ok(updatedRequest); // 변경된 요청 반환
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 친구 요청 목록 조회 API
    @GetMapping("/{id}/friends/request")
    public ResponseEntity<?> getFriendRequest(@PathVariable Long id) {
        try {
            List<FriendResponseDto> requests = friendService.getFriendRequests(id);
            return ResponseEntity.ok(requests); // 요청 목록 반환
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }


    // 1. 친구 목록 조회 (로그인 상태,  ACCEPTED 상태만)
    @GetMapping()
    public ResponseEntity<Object> getMyFriends(@RequestParam(defaultValue = "0") int pageNo,
                                             @RequestParam(defaultValue = "10") int pageSize,
                                             HttpServletRequest request) {

        // request에서 인증된 사용자 정보 가져오기
        User currentUser = (User) request.getAttribute("user");

        // 인증되지 않은 경우 처리
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 인증되지 않은 경우
        }

        // 로그인한 사용자와 요청한 사용자 ID가 일치하지 않는경우

        if (!currentUser.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // 수락된 친구만 불러오기
        List<MyFriendResponseDto> response = friendService.getMyFriends(pageNo, pageSize, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
        try {

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }

    }


    // 2. 친구의 게시물 조회 (로그인된 사용자가 친구의 게시물만 조회할 수 있도록)
    @GetMapping("/posts")
    public ResponseEntity<?> getFriendPosts(
            @PathVariable Long userId,
            @PathVariable Long friendId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        // request에서 인증된 사용자 정보 가져오기
        User currentUser = (User) request.getAttribute("user");

        // null 체크
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 인증되지 않은 경우
        }

        // 로그인한 사용자와 요청한 사용자 ID가 일치하는지 확인
        if (!currentUser.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // 친구의 게시물 조회
        PostResponseDto postResponse = friendService.getFriendPosts(friendId, page, size);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);

    }


    // 3. 친구 삭제
    @DeleteMapping()
    public ResponseEntity<String> deleteFriend(
            @PathVariable Long id,
            @PathVariable Long friendId,
            HttpServletRequest request) {

        // 로그인된 사용자 정보 가져오기
        User currentUser = (User) request.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 인증되지 않은 경우
        }

        if (!currentUser.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        friendService.deleteFriend(id, friendId);
        return ResponseEntity.ok("{\"message\":\"Friend deleted successfully\"}");
    }
}

