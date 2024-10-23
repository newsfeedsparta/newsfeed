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

public interface FriendRepository extends JpaRepository<Friend, Long> {



    //데이터 베이스에 직접 요청하는 쿼리어노테이션

    @Query("SELECT friend FROM Friend friend WHERE friend.requestor.id = :userId AND friend.status = :status")
    Page<Friend> findFriends(@Param("userId") Long userId, @Param("status") FriendStatus status, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Friend WHERE (Friend.requestor.id = :requestorId AND Friend.receiver.id = :receiverId) OR (Friend.requestor.id = :receiverId AND Friend.receiver.id = :requestorId)")
    void deleteFriendship(@Param("requestorId") Long requestorId, @Param("receiverId") Long receiverId);




}
