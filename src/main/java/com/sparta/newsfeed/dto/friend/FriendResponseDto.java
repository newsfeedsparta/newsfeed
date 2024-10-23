package com.sparta.newsfeed.dto.friend;

import com.sparta.newsfeed.entity.Friend;
import com.sparta.newsfeed.entity.FriendStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FriendResponseDto {
    private Long id; // 친구 요청 ID
    private Long requesterId; //  요청을 보낸 사용자 ID
    private Long receiverId; // 요청을 받은 사용자 ID
    private FriendStatus status; // 요청 상태 (PENDING, ACCEPTED, REJECTED)

    public FriendResponseDto(Friend friend) {
        this.id = friend.getId();
        this.requesterId = friend.getRequesterId();
        this.receiverId = friend.getReceiverId();
        this.status = friend.getStatus();
    }
}
