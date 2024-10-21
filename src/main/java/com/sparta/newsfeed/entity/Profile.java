package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Profile extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @Size(max = 500)
    @NotEmpty
    @Column(name = "selfIntroduction")
    private String selfIntroduction;
}
