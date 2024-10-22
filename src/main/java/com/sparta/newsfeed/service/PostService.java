package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.Post.PostRequestDto;
import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.entity.Post;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.exception.PostNotFoundException;
import com.sparta.newsfeed.repository.PostRepository;
import com.sparta.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시물 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Post post = new Post();
        post.setUser(user);
        post.setContents(postRequestDto.getContents());
        Post savedPost = postRepository.save(post);

        return PostResponseDto.fromEntity(savedPost);
    }

    // 모든 게시물 조회
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 키워드로 게시물 조회
    public List<PostResponseDto> searchPosts(String keyword) {
        return postRepository.findByContentsContaining(keyword).stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 게시물 수정
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시물을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("게시물을 수정할 권한이 없습니다.");
        }

        post.setContents(postRequestDto.getContents());
        Post updatedPost = postRepository.save(post);

        return PostResponseDto.fromEntity(updatedPost);
    }

    // 게시물 삭제
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시물을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new RuntimeException("게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }


}
