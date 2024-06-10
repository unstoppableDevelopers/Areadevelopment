package com.sparta.areadevelopment.repository;

import com.sparta.areadevelopment.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    // 탈퇴 여부를 확인하고 User를 가져오는 쿼리문
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.status = :status")
    Optional<User> findUserByIdAndStatus(@Param("id") Long id, @Param("status") String status);
}
