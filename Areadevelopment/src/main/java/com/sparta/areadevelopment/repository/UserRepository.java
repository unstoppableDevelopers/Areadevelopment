package com.sparta.areadevelopment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.sparta.areadevelopment.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
