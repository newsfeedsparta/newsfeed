package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.Post.PostRequestDto;
import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.entity.Post;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.exception.PostNotFoundException;
import com.sparta.newsfeed.repository.FriendRepository;
import com.sparta.newsfeed.repository.PostRepository;
import com.sparta.newsfeed.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;

    // 게시물 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");

        Post post = new Post();
        post.setUser(user);
        post.setContents(postRequestDto.getContents());
        Post savedPost = postRepository.save(post);

        return PostResponseDto.fromEntity(savedPost);
    }

    // 모든 게시물 조회
    public List<PostResponseDto> getAllPosts(int page, int size) {
        // User user = (User) request.getAttribute("user");
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
            return postRepository.findAll(pageable).stream()
                    .map(PostResponseDto::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("모든 게시물 조회 중 오류가 발생했습니다.", e);
        }

    }

    // 내 게시물 조회
    public List<PostResponseDto> getMyPosts(int page, int size, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
        return postRepository.findByUserId(user.getId(),pageable).stream()
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
    public PostResponseDto updatePost(Long postId, PostRequestDto postRequestDto, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시물을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("게시물을 수정할 권한이 없습니다.");
        }

        post.setContents(postRequestDto.getContents());
        Post updatedPost = postRepository.save(post);

        return PostResponseDto.fromEntity(updatedPost);
    }

    // 게시물 삭제
    public void deletePost(Long postId, HttpServletRequest request) {
        User user = (User) request.getAttribute("user");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시물을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }


//    // 내 게시물 조회 (페이징, 수정일 기준으로 내림차순 정렬)
//    @Transactional
//    public PostResponseDto getPostsByUserId(Long userId, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by("modifiedAt").descending());
//        Page<Post> postsPage = postRepository.findByUserId(userId, pageable); // userId로 게시물 조회
//
//        PostResponseDto response = new PostResponseDto();
//        response.setPage(page);
//        response.setTotalPages(postsPage.getTotalPages());
//        response.setContents(postsPage.getContent().stream()
//                .map(post -> {
//                    PostResponseDto.PostInfo postInfo = new PostResponseDto.PostInfo();
//                    postInfo.setPostId(post.getId());
//                    postInfo.setContents(post.getContents());
//                    postInfo.setCreatedAt(Timestamp.valueOf(post.getCreatedAt()));
//                    return postInfo;
//                }).toList().toString());
//        return response;
//    }


}
