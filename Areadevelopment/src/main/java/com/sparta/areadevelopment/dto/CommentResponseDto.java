package com.sparta.areadevelopment.dto;


import com.sparta.areadevelopment.entity.Comment;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private Long boardId;
    private Long userId;
    private String content;
    private Long likeCount;
    private LocalDateTime deleteAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.boardId = comment.getBoard().getId();
        this.userId = comment.getUser().getId();
        this.content = comment.getContent();
        this.likeCount = comment.getLikeCount();
        this.deleteAt = comment.getDeletedAt();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
