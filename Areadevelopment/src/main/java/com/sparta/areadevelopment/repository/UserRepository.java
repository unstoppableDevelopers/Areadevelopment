package com.sparta.areadevelopment.repository;


import com.sparta.areadevelopment.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // create 검증 로직
    Optional<User> findByUsernameOrNicknameOrEmail(String username, String nickname, String email);
}
