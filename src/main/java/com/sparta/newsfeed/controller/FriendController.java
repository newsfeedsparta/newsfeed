package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.friend.FriendRequestDto;
import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class FriendController {

    @Autowired
    private FriendService friendService; // 친구 서비스 주입

    // 친구 요청 생성 API
    @PostMapping("/{id}/friends/{friendId}/request")
    public ResponseEntity<FriendResponseDto> createFriendRequest(
            @PathVariable Long id,
            @RequestBody FriendRequestDto friendRequestDto) {
        FriendResponseDto response = friendService.createFriendRequest(id, friendRequestDto);
        return ResponseEntity.ok(response); // 요청 성공시 응답 반환
    }

    // 친구 요청 상태 변경 API
    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<FriendResponseDto> updateFriendRequest(@PathVariable Long requestId, @RequestParam String status) {
        FriendResponseDto updatedRequest = friendService.updateFriendRequestStatus(requestId, status);
        return ResponseEntity.ok(updatedRequest); // 변경된 요청 반환
    }

    // 친구 요청 목록 조회 API
    @GetMapping("/{id}/friends/request")
    public ResponseEntity<List<FriendResponseDto>> getFriendRequest(@PathVariable Long id) {
        List<FriendResponseDto> requests = friendService.getFriendRequests(id);
        return ResponseEntity.ok(requests); // 요청 목록 반환
    }
}
