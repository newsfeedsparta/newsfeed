package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.friend.FriendRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Friend extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 친구 요청 ID

    @Column(nullable = false)
    private Long requesterId; // 요청을 보낸 사용자

    @Column(nullable = false)
    private Long receiverId; // 요청을 받은 사용자

    @Enumerated(EnumType.STRING)
    private FriendStatus status; // 요청 상태

    public static Friend from(FriendRequestDto req){
        Friend friend = new Friend();
        friend.init(req);
        return friend;
    }

    private void init(FriendRequestDto req){
        this.requesterId = req.getRequesterId();
        this.receiverId = req.getReceiverId();
    }

}

