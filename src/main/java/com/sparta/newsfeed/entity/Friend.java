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
    private Long id;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @NotBlank
    @Column(name = "receiverId")
    private Long receiverId;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

}
