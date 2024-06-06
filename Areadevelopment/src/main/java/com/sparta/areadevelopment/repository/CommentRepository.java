package com.sparta.areadevelopment.repository;


import com.sparta.areadevelopment.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByDeletedAtNull();
}
