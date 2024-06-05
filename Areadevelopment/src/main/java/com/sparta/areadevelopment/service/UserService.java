package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signUp(SignupRequestDto requestDto) throws Exception{
        User user = new User();

        user.setUserInfo(
            requestDto.getUsername(), // unique
            requestDto.getNickname(), // unique
            requestDto.getPassword(), // 여기서 암호화 한 부분을 넣습니다.
            requestDto.getEmail(), // unique
            requestDto.getInfo()
        );

        try{
            userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new Exception("User or Nickname or Email already exists");
        }
    }

    public User signOut(Long userId, SignOutRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("Failed to find comment with id," + userId)
        );

        if(user == null
            || !Objects.equals(user.getUsername(), requestDto.getUsername())
            || !Objects.equals(user.getPassword(), requestDto.getPassword())
        ){
            throw new IllegalArgumentException("Invalid username or password.");
        }else{
            user.softDelete(); // 동시 처리
            userRepository.delete(user);
        }

        return user;
    }
}
