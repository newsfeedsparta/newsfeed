package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.profile.CreateProfileRequestDto;
import com.sparta.newsfeed.dto.profile.ProfileResponseDto;
import com.sparta.newsfeed.dto.profile.UpdateProfileRequestDto;
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
    @OneToOne  //테이블 인덱스(키)가 이상하게 걸려있었음, 테이블 삭제하고 다시 실행했더니 작동
    @JoinColumn(name = "userId")
    private User user; // = new User(); 객체에 값이 안담겨있는데 user 엔티티에 있는 정보를 가져올 수 있나?

    @Size(max = 500)
    @NotEmpty
    @Column
    private String selfIntroduction;

    //프로필 생성
    public static Profile from(CreateProfileRequestDto requestDto, User user) {
        Profile profile = new Profile();
        profile.init(requestDto, user);
        return profile;
    }

    //프로필 데이터 초기화
    public void init(CreateProfileRequestDto requestDto, User user) {
        this.user = user;
        this.selfIntroduction = requestDto.getSelfIntroduction();
    }

    public void update(UpdateProfileRequestDto ureq){
        this.selfIntroduction = ureq.getSelfIntroduction();
    }

    public ProfileResponseDto to(){
        return new ProfileResponseDto(
                id,
                user.to(),
                selfIntroduction,
                getCreatedAt(),
                getModifiedAt()
        );
    }
}
