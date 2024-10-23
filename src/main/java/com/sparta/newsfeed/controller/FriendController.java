package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.dto.friend.FriendRequestDto;
import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.service.FriendService;
import com.sparta.newsfeed.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class FriendController {
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
    
    // @Autowired 추가
    private PostService postService;

    // 1. 친구 목록 조회 (로그인 상태, 페이징 처리, ACCEPTED 상태만)
    @GetMapping
    public ResponseEntity<FriendResponseDto> getFriends(@PathVariable Long id,
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

        if (!currentUser.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // 수락된 친구만 불러오기
        FriendResponseDto response = friendService.getFriends(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    // 2. 친구의 게시물 조회 (로그인된 사용자가 친구의 게시물만 조회할 수 있도록)
    @GetMapping("/{friendId}/posts")
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

        // 친구 관계가 ACCEPTED 상태인지 확인
        boolean isFriendAccepted = friendService.isFriendAccepted(userId, friendId);
        if (!isFriendAccepted) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 친구 관계가 아닌 경우
        }

        // 친구의 게시물 조회
        PostResponseDto postResponse = friendService.getFriendsPosts(friendId, page, size);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }


    // 3. 친구 삭제
    @DeleteMapping("/{friendId}")
    public ResponseEntity<String> deleteFriend(
            @PathVariable Long id,
            @PathVariable Long friendId,
            HttpServletRequest request) {

        // 로그인된 사용자 정보 가져오기
        User currentUser = (User) request.getAttribute("user");
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (!currentUser.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        friendService.deleteFriend(id, friendId);
        return ResponseEntity.ok("{\"message\":\"Friend deleted successfully\"}");
    }
}

