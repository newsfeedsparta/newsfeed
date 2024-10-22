package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.entity.Friend;
import com.sparta.newsfeed.entity.FriendStatus;
import com.sparta.newsfeed.entity.Post;
import com.sparta.newsfeed.repository.FriendRepository;
import com.sparta.newsfeed.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private PostRepository postRepository;

    public FriendResponseDto getFriends(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // 1. 친구 목록 조회 (ACCEPTED 상태인 친구만)
        Page<Friend> friendsPage = friendRepository.findByUserIdAndStatus(userId, FriendStatus.ACCEPTED, pageable);

        FriendResponseDto response = new FriendResponseDto();
        response.setPage(page);
        response.setTotalPages(friendsPage.getTotalPages());
        response.setFriends(friendsPage.getContent().stream()
                .map(friend -> {
                    FriendResponseDto.FriendInfo info = new FriendResponseDto.FriendInfo();
                    info.setReceiverId(friend.getReceiverId());
                    // 여기서 추가적인 사용자 정보 (id, 이름, 자기소개) 조회 로직 필요
                    return info;
                }).toList());
        return response;
    }

    // 2. 친구 게시물 조회

    // 3. 친구 삭제
    public void deleteFriend(Long userId, Long friendId) {
        //현재 사용자의 친구 관계 삭제
        friendRepository.deleteByUserIdAndFriendId(userId, friendId);
        // 상대방의 친구 목록에서 삭제
        friendRepository.deleteByUserIdAndFriendId(friendId, userId);
        //  A가 B를 친구 목록에서 삭제할 경우, B의 친구 목록에서도  A가 삭제됨.
        // 친구 삭제 후 다시 친구 요청을 할 수 있음. (데이터베이스에서 기록 삭제)

    }

}

