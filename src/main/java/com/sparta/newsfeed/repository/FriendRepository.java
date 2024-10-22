package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByReceiverId(Long receiverId); // 수신자 ID로 친구 요청 목록 조회
}
