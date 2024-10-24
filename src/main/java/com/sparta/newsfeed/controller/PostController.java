package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.ErrorResponseDto;
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
    @PostMapping()
    public ResponseEntity<Object> createPost(@RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        try {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(postService.createPost(postRequestDto, request));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 모든 게시물 조회
    @GetMapping()
    public ResponseEntity<Object> getPosts(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
                                           @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
                                           HttpServletRequest request) {
        try {
            List<PostResponseDto> responseDto = postService.getAllPosts(pageNum, pageSize, request);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    //내 게시물 조회
    @GetMapping("/my")
    public ResponseEntity<Object> getMyPosts(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNum,
                                             @RequestParam(required = false, defaultValue = "10", value = "pageSize") int pageSize,
                                             HttpServletRequest request) {
        try {
            List<PostResponseDto> responseDto = postService.getMyPosts(pageNum, pageSize, request);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }


    // 키워드로 게시물 검색
    @GetMapping("/search")
    public ResponseEntity<Object> searchPosts(@RequestParam String keyword) {
        try {
            List<PostResponseDto> posts = postService.searchPosts(keyword);
            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 게시물 수정
    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto, HttpServletRequest request) {
        try {
            PostResponseDto updatedPost = postService.updatePost(id, postRequestDto, request);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

    // 게시물 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePost(@PathVariable Long id, HttpServletRequest request) {
        try {
            postService.deletePost(id, request);
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body("게시물 삭제 성공!!");
        }
        catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto(e.getMessage()));
        }
    }

//    // 내 게시물 조회 (로그인된 사용자만)
//    @GetMapping("/my/posts")
//    public ResponseEntity<PostResponseDto> getMyPosts(
//            @PathVariable Long userId,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            HttpServletRequest request) {
//
//        // 현재 사용자 정보를 request에서 가져옵니다
//        User currentUser = (User) request.getAttribute("user");
//
//        // null 체크
//        if (currentUser == null) {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 인증되지 않은 경우
//        }
//
//        // 로그인한 사용자와 요청한 사용자 ID가 일치하는지 확인
//        if (!currentUser.getId().equals(userId)) {
//            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 권한이 없는 경우
//        }
//
//        // 내 게시물 조회
//        PostResponseDto postResponse = postService.getPostsByUserId(userId, page, size);
//        return new ResponseEntity<>(postResponse, HttpStatus.OK);
//    }

}
