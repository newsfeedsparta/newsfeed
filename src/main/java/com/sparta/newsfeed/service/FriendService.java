package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.dto.friend.FriendRequestDto;
import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.dto.friend.MyFriendResponseDto;
import com.sparta.newsfeed.entity.*;
import com.sparta.newsfeed.repository.FriendRepository;
import com.sparta.newsfeed.repository.PostRepository;
import com.sparta.newsfeed.repository.ProfileRepository;
import com.sparta.newsfeed.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;


    // 친구 요청 생성 및 상태 설정
    public FriendResponseDto createFriendRequest(FriendRequestDto friendRequestDto) {
        User requester = userRepository.findById(friendRequestDto.getRequesterId())
                .orElseThrow(() -> new RuntimeException("요청하는 사용자를 찾을 수 없습니다."));
        User receiver = userRepository.findById(friendRequestDto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("요청 받는 사용자를 찾을 수 없습니다."));

        // 요청자가 수신자에게 또는 수신자가 요청자에게 이미 요청이 존재하는지 확인
        boolean exists = friendRepository.existsByRequesterIdAndReceiverId(friendRequestDto.getRequesterId(), friendRequestDto.getReceiverId())
                || friendRepository.existsByRequesterIdAndReceiverId(friendRequestDto.getReceiverId(), friendRequestDto.getRequesterId());

        if (exists) {
            throw new IllegalArgumentException("이미 존재하는 요청입니다.");
        }

        Friend friendRequest = Friend.from(friendRequestDto);
        friendRepository.save(friendRequest); // 친구 요청 저장

        return friendRequest.to();
    }

    //  친구 요청 상태 변경
    public FriendResponseDto updateFriendRequestStatus(Long requestId, String status) {
        Friend friendRequest = friendRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("친구 요청을 찾을 수 없습니다."));
        friendRequest.setStatus(FriendStatus.valueOf(status)); // 상태 변경
        friendRepository.save(friendRequest); // 변경된 요청 저장

        return friendRequest.to(); //변환하여 반환
    }

    // 친구 요청을 조회
    public List<FriendResponseDto> getFriendRequests(int pageNo, int pageSize, HttpServletRequest req) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        User user = (User) req.getAttribute("user");

        Page<Friend> friends = friendRepository.findByRequesterIdOrReceiverId(user.getId(), user.getId(), pageable);

        return friends.stream().map(FriendResponseDto::new).toList();
    }

    // 내 친구를 조회
    public List<MyFriendResponseDto> getMyFriends(int pageNo, int pageSize, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "modifiedAt"));

        User user = (User) request.getAttribute("user");

        Page<Friend> friends = friendRepository.findFriends(user.getId(), user.getId(), FriendStatus.ACCEPTED, pageable);

        List<Long> friendIds = friends.stream()
                .map(friend -> friend.getRequesterId().equals(user.getId()) ? friend.getReceiverId() : friend.getRequesterId())
                .collect(Collectors.toList());

        List<User> users = userRepository.findUsersById(friendIds);

        return users.stream().map(userEntity -> {
            System.out.println(userEntity.getId());
            Profile profile = profileRepository.findByUserId(userEntity.getId()).orElseThrow(
                    () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
            );
            String selfIntroduction = profile != null ? profile.getSelfIntroduction() : null;

            return new MyFriendResponseDto(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(), selfIntroduction);
        }).toList();
    }

    // 친구 게시물 조회
    public List<PostResponseDto> getFriendPosts(Long friendId, int pageNo, int pageSize, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");

        boolean canGetPost = friendRepository.existsByRequesterIdAndReceiverIdAndStatus(user.getId(), friendId, FriendStatus.ACCEPTED) ||
                friendRepository.existsByRequesterIdAndReceiverIdAndStatus(friendId, user.getId(), FriendStatus.ACCEPTED);

        System.out.println(friendRepository.existsByRequesterIdAndReceiverIdAndStatus(user.getId(), friendId, FriendStatus.ACCEPTED) );
        System.out.println(friendRepository.existsByRequesterIdAndReceiverIdAndStatus(friendId, user.getId(), FriendStatus.ACCEPTED));
        if (!canGetPost) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findByUserId(friendId, pageable);

        return posts.stream().map(PostResponseDto::fromEntity).toList();
    }

    // 친구 삭제
    public void deleteFriend(Long requestId, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");

        Friend friend = friendRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("친구 요청을 찾을 수 없습니다."));

        if (!friend.getRequesterId().equals(user.getId()) && !friend.getReceiverId().equals(user.getId())) {
            throw new IllegalArgumentException("이 친구 요청을 삭제할 권한이 없습니다.");
        }

        friendRepository.delete(friend);
    }
}


