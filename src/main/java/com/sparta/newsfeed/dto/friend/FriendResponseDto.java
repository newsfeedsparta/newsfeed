package com.sparta.newsfeed.dto.friend;

import lombok.Data;

import java.util.List;

@Data
public class FriendResponseDto {
    private Long requestId; // 친구 요청 ID
    private Long userId; //  요청을 보낸 사용자 ID
    private Long receiverId; // 요청을 받은 사용자 ID
    private String status; // 요청 상태 (PENDING, ACCEPTED, REJECTED)

    private List<FriendInfo> friends; // 친구 목록 리스트
    private int page; // 현재 페이지
    private int totalPages; // 총 페이지 수

    // 친구 정보
    public static class FriendInfo {
        private Long receiverId; // 친구의 사용자 ID
        private String username; // 친구의 사용자 이름
        private String selfIntroduction; // 자기소개
    }
}
