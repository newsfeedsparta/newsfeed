package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 50)
    @NotBlank
    @Column (name = "username")
    private String username;

    @Email
    @Column (name = "email")
    private String email;

    @NotBlank
    @Column (name = "password")
    private String password;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL) // 프로필이랑 1:1 관계
    private Profile profile;

    @OneToMany(mappedBy = "post") // 포스트랑 1:n 관계
    private List<Post> posts;

    @OneToMany(mappedBy = "friend")  // 친구랑 1:n 관계
    private List<Friend> friend;

}
