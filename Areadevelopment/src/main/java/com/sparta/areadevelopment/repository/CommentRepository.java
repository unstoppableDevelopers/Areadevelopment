package com.sparta.areadevelopment.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.areadevelopment.entity.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 좋아요 내역이 저장되었을때 댓글 좋아요 필드값을 증가시키는 쿼리문입니다.
     *
     * @param commentId 댓글 고유번호
     */
    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount + 1 WHERE c.id = :commentId")
    void incrementLikeCount(@Param("commentId") Long commentId);

    /**
     * 좋아요 내역이 삭제되었을때 게시판 좋아요 필드값을 감소시키는 쿼리문입니다.
     *
     * @param commentId 댓글 고유번호
     */
    @Modifying
    @Query("UPDATE Comment c SET c.likeCount = c.likeCount - 1 WHERE c.id = :commentId")
    void decrementLikeCount(@Param("commentId") Long commentId);
}
