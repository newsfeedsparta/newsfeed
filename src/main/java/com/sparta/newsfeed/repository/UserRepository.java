package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // 유저명으로 유저 조회
}
