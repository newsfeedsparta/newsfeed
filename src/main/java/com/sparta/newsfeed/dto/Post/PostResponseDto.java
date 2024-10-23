package com.sparta.newsfeed.dto.Post;

import com.sparta.newsfeed.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private Long userId;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // 개별 Post 엔티티를 PostResponseDto로 변환하는 메서드
    public static PostResponseDto fromEntity(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setContents(post.getContents());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setModifiedAt(post.getModifiedAt());
        return dto;
    }

}


