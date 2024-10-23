package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @NotEmpty
    @Size(max = 500)
    @Column(name = "contents")
    private String contents;


    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 기본값으로 현재 시간 설정

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now(); // 엔티티가 생성될 때 현재 시간으로 설정

    }
}
