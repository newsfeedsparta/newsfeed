package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.friend.FriendRequestDto;
import com.sparta.newsfeed.dto.friend.FriendResponseDto;
import com.sparta.newsfeed.entity.Friend;
import com.sparta.newsfeed.entity.FriendStatus;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.exception.FriendRequestNotFoundException;
import com.sparta.newsfeed.repository.FriendRepository;
import com.sparta.newsfeed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

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
                .orElseThrow(() -> new FriendRequestNotFoundException("Friend request not found for receiver ID: " + receiverId));
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
}