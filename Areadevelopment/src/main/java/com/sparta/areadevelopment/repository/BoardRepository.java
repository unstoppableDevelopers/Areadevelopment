package com.sparta.areadevelopment.repository;

import com.sparta.areadevelopment.entity.Board;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Optional<Board> findByIdAndDeletedAtIsNull(Long id);
    //DeletedAt이 Not Null 인 경우 삭제된 게시글
    List<Board> findAllByDeletedAtIsNullOrderByCreatedAtDesc();
}
