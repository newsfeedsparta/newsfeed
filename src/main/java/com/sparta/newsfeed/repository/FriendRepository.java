package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Friend;
import com.sparta.newsfeed.entity.FriendStatus;
import com.sparta.newsfeed.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    // 수락된 친구 목록 조회
    Page<Friend> findByUserIdAndStatus(Long userId, FriendStatus status, Pageable pageable);

    // 친구 삭제 (두 메소드를 작성해야 함)
    void deleteByUserIdAndFriendId(Long userId, Long friendId); // A가 B를 삭제할 때

    void deleteByFriendIdAndUserId(Long friendId, Long userId); // B가 A를 삭제할 때

    //수신자 ID로 친구 요청 목록 조회
    List<Friend> findByReceiverId(Long receiverId);

    // 친구 ID만 조회
    List<Long> findAcceptedFriendId(Long userId);

    // 친구 게심물 조회
    Page<Post> findByFreindPosts(List<Long> acceptedFriendId, Pageable pageable);
}
