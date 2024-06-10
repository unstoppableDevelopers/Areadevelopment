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

    //댓글 등록
    public CommentResponseDto addComment(String username, Long boardId,
            CommentRequestDto requestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NullPointerException("선택한 사용자는 없습니다."));
        Board board = findBoardById(boardId);
        Comment comment = new Comment(requestDto.getContent(), board, user);
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    //특정 게시물 댓글 모두 조회
    public List<CommentResponseDto> getAllComments(Long boardId) {
        return commentRepository.findByDeletedAtNullAndBoardIdOrderByCreatedAtDesc(boardId)
                .map(Collection::stream).orElseGet(Stream::empty).map(CommentResponseDto::new)
                .toList();
    }

    //특정 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(String username, Long commentId,
            CommentRequestDto requestDto) {
        Comment comment = findCommentById(commentId);
        if (comment.isDeleted()) {
            throw new IllegalArgumentException("선택한 댓글은 삭제되어 있습니다.");
        }
        if (!comment.isCommentAuthor(username)) {
            throw new IllegalArgumentException("선택한 댓글은 다른 사용자가 작성한 댓글입니다.");
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

    //특정 댓글 삭제
    @Transactional
    public String deleteComment(String username, Long commentId) {
        Comment comment = findCommentById(commentId);
        if (comment.isDeleted()) {
            throw new IllegalArgumentException("선택한 댓글은 삭제되어 있습니다.");
        }
        if (!comment.isCommentAuthor(username)) {
            throw new IllegalArgumentException("선택한 댓글은 다른 사용자가 작성한 댓글입니다.");
        }
        comment.delete();
        return "댓글 삭제 성공";
    }

    private Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NullPointerException("선택한 게시물은 없습니다."));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NullPointerException("선택한 댓글은 없습니다."));
    }

}
