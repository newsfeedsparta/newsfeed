package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.Post.PostResponseDto;
import com.sparta.newsfeed.dto.friend.FriendRequestDto;
import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.entity.Friend;
import com.sparta.newsfeed.entity.FriendStatus;
import com.sparta.newsfeed.entity.Post;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.exception.FriendRequestNotFoundException;
import com.sparta.newsfeed.repository.FriendRepository;
import com.sparta.newsfeed.repository.PostRepository;
import com.sparta.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private  final FriendRepository friendRepository;
    private  final UserRepository userRepository;
    private  final PostRepository postRepository;


    // 친구 요청 생성 및 상태 설정
    public FriendResponseDto createFriendRequest(Long requestorId, FriendRequestDto friendRequestDto) {
        Long friendId = friendRequestDto.getFriendId();
        User requestor = userRepository.findById(requestorId)
                .orElseThrow(() -> new RuntimeException("Requestor not found"));
        User receiver = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));


        Friend friendRequest = new Friend();
        friendRequest.setRequestor(requestor); // 요청자 설정
        friendRequest.setReceiver(receiver); // 수신자 설정
        friendRequest.setStatus(FriendStatus.PENDING); // 상태 설정, 기본값 PENDING
        friendRepository.save(friendRequest); // 친구 요청 저장

        // FriendResponseDto 로 변환하여 반환
        return convertToDto(friendRequest);
    }

    //  친구 요청 상태 변경
    public FriendResponseDto updateFriendRequestStatus(Long requestorId, String status) {
        Friend friendRequest = friendRepository.findById(requestorId)
                .orElseThrow(() -> new IllegalArgumentException("친구 요청을 찾을 수 없습니다."));
        friendRequest.setStatus(FriendStatus.valueOf(status)); // 상태 변경
        friendRepository.save(friendRequest); // 변경된 요청 저장

        return convertToDto(friendRequest); //변환하여 반환
    }


    // Friend 객체를 FriendResponseDto 로 변환
    private FriendResponseDto convertToDto(Friend friend) {
        FriendResponseDto friendResponseDto = new FriendResponseDto();
        friendResponseDto.setRequestId(friend.getId());
        friendResponseDto.setUserId(friend.getRequestor().getId());
        friendResponseDto.setReceiverId(friend.getReceiver().getId());
        friendResponseDto.setStatus(friend.getStatus().name());
        return friendResponseDto;
    }


    // 1. 친구 목록 조회 (ACCEPTED 상태인 친구만)
    public FriendResponseDto getFriends(Long userId, int page, int size) {

        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);

        // ACCEPTED 상태인 친구만 페이징 처리하여 가져오기
        Page<Friend> friendsPage = friendRepository.findFriends(userId, FriendStatus.ACCEPTED, pageable);

        // FriendResponseDto로 변환하여 반환
        FriendResponseDto response = new FriendResponseDto();


        // 친구 목록 설정
        List<FriendResponseDto.FriendInfo> friendInfoList = friendsPage.stream()
                .map(friend -> {
                    FriendResponseDto.FriendInfo info = new FriendResponseDto.FriendInfo();
                    info.setReceiverId(friend.getReceiver().getId());  // 친구의 사용자 ID
                    info.setUsername(friend.getReceiver().getUsername());  // 친구의 사용자 이름
                    info.setSelfIntroduction(friend.getReceiver().getProfile().getSelfIntroduction());  // 친구의 자기소개
                    return info;
                })
                .collect(Collectors.toList());

        response.setFriends(friendInfoList);  // 친구 목록 설정

        return response;
    }

   //2. 친구 게시물 조회
    public PostResponseDto getFriendPosts(Long friendId, int page, int size) {
        // Pageable 객체 생성
        Pageable pageable = PageRequest.of(page, size);


        Page<Post> postsPage = postRepository.findFriendPosts(friendId, pageable);

        // PostResponseDto로 변환하여 반환
        PostResponseDto response = new PostResponseDto();

        // 게시물 목록 설정
        List<PostResponseDto.PostInfo> postInfoList = postsPage.stream()
                .map(post -> {
                    PostResponseDto.PostInfo info = new PostResponseDto.PostInfo();
                    info.setPostId(post.getId());  // 게시물 ID
                    info.setContents(post.getContents());  // 게시물 내용
                    info.setCreatedAt(Timestamp.valueOf(post.getCreatedAt()));  // 게시물 작성 시간
                    return info;
                })
                .collect(Collectors.toList());

        response.setPosts(postInfoList);  // 게시물 목록 설정
        response.setPage(page);  // 현재 페이지
        response.setTotalPages(postsPage.getTotalPages());  // 총 페이지 수

        return response;
    }





    // 3. 친구 삭제

    public void deleteFriend(Long userId, Long friendId) {
        // 친구 관계 삭제
        friendRepository.deleteFriendship(userId, friendId);
    }

    }


