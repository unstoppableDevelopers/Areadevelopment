package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.entity.Timestamped;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public User signOut(Long userId, SignOutRequestDto requestDto) {
        // userId를 통해서 user를 찾는다.
        User user = userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("Failed to find comment with id," + userId)
        );

        // 유효성 검사 부분
        if( !Objects.equals(user.getUsername(), requestDto.getUsername())
            && !Objects.equals(user.getPassword(), requestDto.getPassword())
        ){
            throw new IllegalArgumentException("Invalid username or password.");
        }else{
            user.softDelete(); // 동시 처리
        }

        return user;
    }
}
