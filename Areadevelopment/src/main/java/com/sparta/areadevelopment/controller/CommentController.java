package com.sparta.areadevelopment.controller;


import com.sparta.areadevelopment.dto.CommentRequestDto;
import com.sparta.areadevelopment.dto.CommentResponseDto;
import com.sparta.areadevelopment.service.CommentService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/boards")
public class CommentController {

    private final CommentService commentService;

    private CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{boardId}/comments")
    public CommentResponseDto addComment(@AuthenticationPrincipal String username,
            @PathVariable Long boardId, @RequestBody CommentRequestDto requestDto) {
        return commentService.addComment(username, boardId, requestDto);
    }

    @GetMapping("/comments")
    public List<CommentResponseDto> getAllComments() {
        return commentService.getAllComments();
    }

    @PutMapping("/{boardId}/comments/{commentId}")
    public CommentResponseDto updateComment(@AuthenticationPrincipal String username,
            @PathVariable Long boardId, @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto) {
        return commentService.updateComment(username, boardId, commentId, requestDto);
    }

    @DeleteMapping("/{boardId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal String username,
            @PathVariable Long boardId, @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(username, boardId, commentId));
    }
}
