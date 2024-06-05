package com.sparta.areadevelopment.repository;


import com.sparta.areadevelopment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
