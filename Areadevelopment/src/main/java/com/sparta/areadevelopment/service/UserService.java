package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.dto.UpdateUserDto;
import com.sparta.areadevelopment.dto.UserInfoDto;
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

    public void signUp(SignupRequestDto requestDto) throws Exception {
        User user = new User(
            requestDto.getUsername(), // unique
            requestDto.getNickname(), // unique
            requestDto.getPassword(), // 여기서 암호화 한 부분을 넣습니다.
            requestDto.getEmail(), // unique
            requestDto.getInfo()
        );

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("User or Nickname or Email already exists");
        }
    }

    public UserInfoDto getUser(Long userId, String password) {
        // password 확인
        User user = checkUser(userId);

        // 시큐리티에서 PasswordEncoder를 통해 확인 후 리턴으로 변경
        if (user == null || !Objects.equals(user.getPassword(), password)) {
            throw new IllegalArgumentException("Password does not match");
        }

        return new UserInfoDto(user.getId(), user.getNickname(),
            user.getInfo(), user.getEmail());
    }

    @Transactional
    public User updateProfile(Long userId, UpdateUserDto requestDto) {
        User user = checkUser(userId);

        // 유효성 검사 부분
        if (!Objects.equals(user.getPassword(), requestDto.getPassword())
        ) {
            throw new IllegalArgumentException("Invalid username or password.");
        } else {
            user.updateProfile(requestDto);
            userRepository.save(user);
            return user;
        }
    }

    @Transactional
    public User signOut(Long userId, SignOutRequestDto requestDto) {
        User user = checkUser(userId);

        // 유효성 검사 부분
        if (!Objects.equals(user.getUsername(), requestDto.getUsername())
            && !Objects.equals(user.getPassword(), requestDto.getPassword())
        ) {
            throw new IllegalArgumentException("Invalid username or password.");
        } else {
            user.softDelete(); // 동시 처리
        }

        return user;
    }

    private User checkUser(Long userId) {
        // userId를 통해서 user를 찾는다.
        User user = userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("Failed to find comment with id," + userId)
        );
        return user;
    }


}
