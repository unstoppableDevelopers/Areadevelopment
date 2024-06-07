package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.CommentRequestDto;
import com.sparta.areadevelopment.dto.CommentResponseDto;
import com.sparta.areadevelopment.entity.Board;
import com.sparta.areadevelopment.entity.Comment;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.repository.BoardRepository;
import com.sparta.areadevelopment.repository.CommentRepository;
import com.sparta.areadevelopment.repository.UserRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    public CommentResponseDto addComment(String username, Long boardId,
            CommentRequestDto requestDto) {
        User user = findUserByUsername(username);
        Board board = findBoardById(boardId);
        Comment comment = new Comment(requestDto.getContent(), board, user);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public List<CommentResponseDto> getAllComments() {
        return commentRepository.findByDeletedAtNullOrderByCreatedAtDesc().map(Collection::stream)
                .orElseGet(Stream::empty).map(CommentResponseDto::new).toList();
    }

    @Transactional
    public CommentResponseDto updateComment(String username, Long boardId, Long commentId,
            CommentRequestDto requestDto) {
        Board board = findBoardById(boardId);
        Comment comment = findCommentById(commentId);
        checkBoardDeleted(board);
        checkCommentDeleted(comment);
        checkCommentIsInBoard(comment, board);
        checkCommentAuthor(comment, username);
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    @Transactional
    public String deleteComment(String username, Long boardId, Long commentId) {
        Board board = findBoardById(boardId);
        Comment comment = findCommentById(commentId);
        checkBoardDeleted(board);
        checkCommentDeleted(comment);
        checkCommentIsInBoard(comment, board);
        checkCommentAuthor(comment, username);
        comment.delete();
        return "댓글 삭제 성공";
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("선택한 사용자는 없습니다."));
    }

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException("선택한 게시물은 없습니다."));
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NullPointerException("선택한 댓글은 없습니다."));
    }

    public void checkCommentDeleted(Comment comment) {
        if (comment.getDeletedAt() != null) {
            throw new IllegalArgumentException("선택한 댓글은 삭제되어 있습니다.");
        }
    }

    public void checkCommentIsInBoard(Comment comment, Board board) {
        if (!board.getComments().contains(comment)) {
            throw new IllegalArgumentException("선택한 게시물에 선택한 댓글이 없습니다.");
        }
    }

    public void checkBoardDeleted(Board board) {
        if (board.getDeletedAt() != null) {
            throw new IllegalArgumentException("선택한 게시물은 삭제되어 있습니다.");
        }
    }

    public void checkCommentAuthor(Comment comment, String username) {
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("선택한 댓글은 다른 사용자가 작성한 댓글입니다.");
        }
    }
}
