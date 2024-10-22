package com.sparta.newsfeed.dto.friend;

import java.util.List;


import lombok.Data;



@Data
public class FriendResponseDto {
    private List<FriendInfo> friends; // 친구 목록 리스트
    private int page; // 현재 페이지
    private int totalPages; // 총 페이지 수

    @Data
    // 친구 정보
    public static class FriendInfo {
        private Long receiverId; // 친구의 사용자 ID
        private String username; // 친구의 사용자 이름
        private String selfIntroduction; // 자기소개
    }
}
