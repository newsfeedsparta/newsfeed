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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    private FriendRepository friendRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;

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

    // 요청 목록 조회
    public List<FriendResponseDto> getFriendRequests(Long receiverId) {
        List<Friend> requests = friendRepository.findByReceiverId(receiverId);


        return requests.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
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


    // 2, 친구의 게시물 조회 (페이징 처리)
    public PostResponseDto getFriendsPosts(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postsPage = postRepository.findByUserIdIn(friendRepository.findAcceptedFriendIdsByUserId(userId), pageable);

        PostResponseDto response = new PostResponseDto();
        response.setPage(page);
        response.setTotalPages(postsPage.getTotalPages());
        response.setContents(postsPage.getContent().stream()
                .map(post -> {
                    PostResponseDto.PostInfo postInfo = new PostResponseDto.PostInfo();
                    postInfo.setPostId(post.getId());
                    postInfo.setContents(post.getContents());
                    postInfo.setCreatedAt(post.getCreatedAt());
                    return postInfo;
                }).toList().toString());
        return response;
    }





    // 3. 친구 삭제
    public void deleteFriend(Long userId, Long friendId) {
        //현재 사용자의 친구 관계 삭제
        friendRepository.deleteByUserIdAndFriendId(userId, friendId);
        // 상대방의 친구 목록에서 삭제
        friendRepository.deleteByUserIdAndFriendId(friendId, userId);
        //  A가 B를 친구 목록에서 삭제할 경우, B의 친구 목록에서도  A가 삭제됨.
        // 친구 삭제 후 다시 친구 요청을 할 수 있음. (데이터베이스에서 기록 삭제)

    }

    // 친구 관계가 수락되었는지 확인하는 메소드
    public boolean isFriendAccepted(Long userId, Long friendId) {
        return friendRepository.findById(friendId)
                .map(friend -> friend.getStatus() == FriendStatus.ACCEPTED)
                .orElse(false);
    }
}