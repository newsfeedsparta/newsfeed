package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.service.FriendService;
import com.sparta.newsfeed.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{id}/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    // @Autowired 추가
    private PostService postService;

    // 1. 친구 목록 조회 (로그인 상태, 페이징 처리, ACCEPTED 상태만)
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<FriendResponseDto> getFriends(@PathVariable Long id,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size
                                                        //  @AuthenticationPrincipal 추가
                                                        ) {

        // 로그인된 사용자와 요청한 사용자 ID가 일치하는지 확인

        // 수락된 친구만 불러오기
        FriendResponseDto response = friendService.getFriends(id, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // 2. 친구 게시물 조회 추가

    // 3. 친구 삭제 (로그인 상태)
    @DeleteMapping("/{friendId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> deleteFriend(
    @PathVariable Long id, @PathVariable Long friendId) {
        friendService.deleteFriend(id, friendId);
        return ResponseEntity.ok("{\"message\":\"Friend deleted successfully\"}");
    }
}

