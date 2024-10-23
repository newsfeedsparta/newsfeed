package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.user.UserResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 50)
    @NotBlank
    @Column(unique = true)
    private String username;

    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL) // 프로필이랑 1:1 관계
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE) // 포스트랑 1:n 관계
    private List<Post> posts;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)  // 친구랑 1:n 관계
//    private List<Friend> friends;  삭제 -> 친구 조회하는 APi 작성, 자체적으로 조회하도록

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        status = UserStatus.Active;
    }

    public UserResponseDto to() {
        return new UserResponseDto(
                username,
                email
        );
    }


}
