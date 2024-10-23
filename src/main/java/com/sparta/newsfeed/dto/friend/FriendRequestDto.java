package com.sparta.newsfeed.dto.friend;

import lombok.AllArgsConstructor;
import lombok.Data;

// 친구 요청을 보내기 위해 클라이언트에서 요청하는 dto. 요청 받는 친구의 ID (receiverId)만 입력받음.

@Data
@AllArgsConstructor
public class FriendRequestDto {
    private Long requesterId; // 친구 요청을 보낼 사용자 ID
    private Long receiverId; // 친구요청을 받을 사용자
}
