package com.sparta.newsfeed.dto.friend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendResponseDto {
    private Long requestId; // 친구 요청 ID
    private Long userId; //  요청을 보낸 사용자 ID
    private Long receiverId; // 요청을 받은 사용자 ID
    private String status; // 요청 상태 (PENDING, ACCEPTED, REJECTED)
}
