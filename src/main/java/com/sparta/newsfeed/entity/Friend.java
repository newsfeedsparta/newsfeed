package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 친구 요청 ID

    @ManyToOne
    @JoinColumn(name = "requestorId")
    private User requestor; // 요청을 보낸 사용자

    @ManyToOne
    @JoinColumn(name = "receiverId")
    private User receiver; // 요청을 받은 사용자

    @Enumerated(EnumType.STRING)
    private FriendStatus status; // 요청 상태


    public Long getReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }
}

