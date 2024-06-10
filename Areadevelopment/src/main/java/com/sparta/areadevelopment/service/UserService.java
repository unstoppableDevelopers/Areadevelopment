package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.dto.UpdateUserDto;
import com.sparta.areadevelopment.dto.UserInfoDto;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public void signUp(SignupRequestDto requestDto) {

        User user = new User(
                requestDto.getUsername(),
                requestDto.getNickname(),
                bCryptPasswordEncoder.encode(requestDto.getPassword()), // 여기서 암호화 한 부분을 넣습니다.
                requestDto.getEmail(),
                requestDto.getInfo()
        );

        userRepository.save(user);
    }

    public UserInfoDto getUser(Long userId) {
        // 특정 유저 있는 지 확인
        User user = findUser(userId);

        return new UserInfoDto(user.getId(), user.getNickname(),
                user.getInfo(), user.getEmail());
    }

    public List<UserInfoDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private UserInfoDto convertToDto(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                user.getInfo()
        );
    }

    @Transactional
    public User updateProfile(Long userId, UpdateUserDto requestDto) {
        // 유저 ID 있나 확인
        User user = findUser(userId);

        // 유효성 검사 부분
        checkPassword(user, requestDto.getPassword());

        user.updateProfile(requestDto);
        return user;
    }

    // 이 부분은 토큰이 필요한 부분이다.
    @Transactional
    public User signOut(Long userId, SignOutRequestDto requestDto) {
        // user Id 검사
        User user = findUser(userId);

        // 유효성 검사 부분 - password
        checkPassword(user, requestDto.getPassword());
        user.softDelete();

        return user;
    }

    private User findUser(Long userId) {
        // userId를 통해서 user를 찾는다.
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("Failed to find comment with id," + userId)
        );
    }

    private void checkPassword(User user, String password){
        if (!Objects.equals(user.getPassword(),  password)
        ) {
            throw new IllegalArgumentException("Invalid password.");
        }
    }

}
