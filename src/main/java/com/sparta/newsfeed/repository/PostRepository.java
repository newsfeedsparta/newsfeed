package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 키워드로 게시물 조회
    List<Post> findByContentsContaining(String keyword);
    // 내 게시물만 조회
    Page<Post> findByUserId(Long userId, Pageable pageable);
}