package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    // receiverIds에 해당하는 User 목록을 페이징하여 반환하는 메서드
    @Query("SELECT u FROM User u WHERE u.id IN :receiverIds")
    List<User> findUsersById(@Param("receiverIds") List<Long> receiverIds);
}

