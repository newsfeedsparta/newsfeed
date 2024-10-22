package com.sparta.newsfeed.dto.Post;

import com.sparta.newsfeed.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private Long userId;
    private String contents;
    private String createdAt;

    public static PostResponseDto fromEntity(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setContents(post.getContents());
        return dto;
    }

}

