package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.Post.PostRequestDto;
import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;


    @PostMapping("/")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @RequestParam Long userId) {
        PostResponseDto createdPost = postService.createPost(postRequestDto, userId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // 모든 게시물 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 키워드로 게시물 검색
    @GetMapping("/search")
    public ResponseEntity<List<PostResponseDto>> searchPosts(@RequestParam String keyword) {
        List<PostResponseDto> posts = postService.searchPosts(keyword);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, @RequestParam Long userId) {
        PostResponseDto updatedPost = postService.updatePost(id, postRequestDto, userId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @RequestParam Long userId) {
        postService.deletePost(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
