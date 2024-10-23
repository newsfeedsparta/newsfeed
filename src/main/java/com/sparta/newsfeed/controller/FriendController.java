package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.ErrorResponseDto;
import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.dto.friend.FriendRequestDto;
import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.dto.friend.MyFriendResponseDto;
import com.sparta.newsfeed.service.FriendService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService; // 친구 서비스 주입

    // 친구 요청 생성
    @PostMapping()
    public ResponseEntity<Object> createFriendRequest(@RequestBody FriendRequestDto friendRequestDto) {
        try {
            FriendResponseDto response = friendService.createFriendRequest(friendRequestDto);
            return ResponseEntity.ok(response); // 요청 성공시 응답 반환
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 친구 요청 상태 변경
    @PutMapping("/{requestId}")
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

    // 친구 요청 목록 조회
    @GetMapping("/requests")
    public ResponseEntity<Object> getFriendRequest(@RequestParam(defaultValue = "0") int pageNo,
                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                   HttpServletRequest request) {
        try {
            List<FriendResponseDto> requests = friendService.getFriendRequests(pageNo, pageSize, request);
            return ResponseEntity.ok(requests); // 요청 목록 반환
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 내 친구 목록 조회
    @GetMapping("/myFriends")
    public ResponseEntity<Object> getMyFriends(@RequestParam(defaultValue = "0") int pageNo,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               HttpServletRequest request) {
        try {
            List<MyFriendResponseDto> response = friendService.getMyFriends(pageNo, pageSize, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 친구(1명)의 게시물 조회
    @GetMapping("/posts/{friendId}")
    public ResponseEntity<?> getFriendPosts(
            @PathVariable Long friendId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest req) {

        try {
            List<PostResponseDto> res = friendService.getFriendPosts(friendId, pageNo, pageSize, req);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 친구 삭제
    @DeleteMapping("/{requestId}")
    public ResponseEntity<Object> deleteFriend(
            @PathVariable Long requestId,
            HttpServletRequest req) {
        try {
            friendService.deleteFriend(requestId, req);
            return ResponseEntity.ok("친구가 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }
}

