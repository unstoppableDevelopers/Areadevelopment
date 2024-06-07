package com.sparta.areadevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.areadevelopment.entity.Board;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    /**
     * 좋아요 내역이 저장되었을때 게시판 좋아요 필드값을 증가시키는 쿼리문입니다.
     *
     * @param boardId 게시판 고유번호
     */
    @Modifying
    @Query("UPDATE Board b SET b.likeCount = b.likeCount + 1 WHERE b.id = :boardId")
    void incrementLikeCount(@Param("boardId") Long boardId);

    /**
     * 좋아요 내역이 삭제되었을때 게시판 좋아요 필드값을 감소시키는 쿼리문입니다.
     *
     * @param boardId 고유아이디
     */
    @Modifying
    @Query("UPDATE Board b SET b.likeCount = b.likeCount - 1 WHERE b.id = :boardId")
    void decrementLikeCount(@Param("boardId") Long boardId);
}
