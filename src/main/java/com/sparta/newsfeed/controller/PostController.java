package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.Post.PostRequestDto;
import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sparta.newsfeed.entity.User;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;


    // 게시물 생성
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

    // 내 게시물 조회 (로그인된 사용자만)
    @GetMapping("/my/posts")
    public ResponseEntity<PostResponseDto> getMyPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        // 현재 사용자 정보를 request에서 가져옵니다
        User currentUser = (User) request.getAttribute("user");

        // null 체크
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 인증되지 않은 경우
        }

        // 로그인한 사용자와 요청한 사용자 ID가 일치하는지 확인
        if (!currentUser.getId().equals(userId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 권한이 없는 경우
        }

        // 내 게시물 조회
        PostResponseDto postResponse = postService.getPostsByUserId(userId, page, size);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }





}
