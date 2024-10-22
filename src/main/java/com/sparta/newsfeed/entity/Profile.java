package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.profile.ProfileRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "userId")
    private User user; // = new User(); 객체에 값이 안담겨있는데 user 엔티티에 있는 정보를 가져올 수 있나?

    @Size(max = 500)
    @NotEmpty
    @Column(name = "selfIntroduction")
    private String selfIntroduction;


    //프로필 생성
    public static Profile from(ProfileRequestDto requestDto, User user) {
        Profile profile =new Profile();
        profile.initData(requestDto, user);
        return profile;
    }

    //프로필 데이터 초기화
    public void initData(ProfileRequestDto requestDto, User user) {
        this.user = user;
        this.selfIntroduction = requestDto.getSelfIntroduction();
    }
}
