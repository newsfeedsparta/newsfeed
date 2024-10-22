package com.sparta.newsfeed.dto.Post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@Getter
public class PostRequestDto {
    private String contents;
}
