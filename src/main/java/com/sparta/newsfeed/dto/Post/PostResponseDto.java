package com.sparta.newsfeed.dto.Post;

import com.sparta.newsfeed.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDto {
    private Long id;
    private Long userId;
    private String contents;
    private LocalDateTime createdAt;

    // 페이징 관련 필드 추가
    private int currentPage;       // 현재 페이지
    private int totalPages;        // 전체 페이지 수
    private long totalElements;    // 전체 게시물 수

    // 게시물 리스트
    private List<PostResponseDto> contentsList;

    // 날짜 포맷
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // 개별 Post 엔티티를 PostResponseDto로 변환하는 메서드
    public static PostResponseDto fromEntity(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setContents(post.getContents());
        dto.setCreatedAt(LocalDateTime.parse(post.getCreatedAt().format(FORMATTER)));  // createdAt 설정
        return dto;
    }

    // 페이징 정보를 포함한 DTO 생성
    public static PostResponseDto createPagedResponse(int currentPage, int totalPages, long totalElements, List<PostResponseDto> contentsList) {
        PostResponseDto dto = new PostResponseDto();
        dto.setCurrentPage(currentPage);
        dto.setTotalPages(totalPages);
        dto.setTotalElements(totalElements);
        dto.setContentsList(contentsList);
        return dto;
    }

    public void setPage(int page) {
        this.currentPage = page; // 현재 페이지 설정
    }

    public static class PostInfo {
        private Long postId; // 게시물 ID
        private String content; // 게시물 내용
        private LocalDateTime createdAt; // 생성일// Post 엔티티를 PostInfo로 변환하는 메서드

        public static PostInfo fromEntity(Post post) {
            PostInfo postInfo = new PostInfo();
            postInfo.setPostId(post.getId());
            postInfo.setContents(post.getContents());
            postInfo.setCreatedAt(LocalDateTime.parse(post.getCreatedAt().format(FORMATTER))); // 날짜 포맷 설정
            return postInfo;
        } // 게시물 ID 설정

        public void setPostId(Long id) {
            this.postId = id; // postId 필드에 ID 설정
        } // 생성일 설정

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;  // createdAt 필드에 날짜 포맷 설정
        }

        public void setContents(String content) {
            this.content = content; // 필드에 값을 설정
        }
    }

}

