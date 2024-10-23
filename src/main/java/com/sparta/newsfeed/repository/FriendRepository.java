package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Friend;
import com.sparta.newsfeed.entity.FriendStatus;
import com.sparta.newsfeed.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    Page<Friend> findByRequesterIdOrReceiverIdAndStatus(Long requesterId, Long receiverId, FriendStatus status, Pageable pageable);

    Page<Friend> findByRequesterIdOrReceiverId(Long requesterId, Long receiverId, Pageable pageable);

    boolean existsByRequesterIdAndReceiverId(Long requesterId, Long receiverId);


    @Query("SELECT f FROM Friend f WHERE (f.requesterId = :requesterId OR f.receiverId = :receiverId) AND f.status = :status ORDER BY f.modifiedAt DESC")
    Page<Friend> findFriends(@Param("requesterId") Long requesterId, @Param("receiverId") Long receiverId, @Param("status") FriendStatus status, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Friend f WHERE f.requesterId = :requesterId AND f.receiverId = :receiverId AND f.status = :status")
    boolean existsByRequesterIdAndReceiverIdAndStatus(@Param("requesterId") Long requesterId, @Param("receiverId") Long receiverId, @Param("status") FriendStatus status);


}
