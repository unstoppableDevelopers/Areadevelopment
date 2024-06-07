package com.sparta.areadevelopment.repository;

import com.sparta.areadevelopment.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

//임시
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);


}
