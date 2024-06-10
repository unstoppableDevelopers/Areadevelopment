package com.sparta.areadevelopment.repository;

import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.enums.StatusEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    // 탈퇴 여부를 확인하고 User를 가져오는 쿼리문
    Optional<User> findUserByIdAndStatus(Long id, StatusEnum statusEnum);

    Optional<User> findByRefreshToken(String token);

    Optional<User> findUserByUsernameAndStatus(String username, StatusEnum statusEnum);
}
