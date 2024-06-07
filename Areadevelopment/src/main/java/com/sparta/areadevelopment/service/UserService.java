package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.dto.UpdateUserDto;
import com.sparta.areadevelopment.dto.UserInfoDto;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.exception.FieldValidationException;
import com.sparta.areadevelopment.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void signUp(SignupRequestDto requestDto) {

        // unique 값이 들어가면 발생하는 excpetion인데 이것은 어떤 것을 상속을 받은 것이다.
        // 상위 객체를 직접적으로 잡아서 처리

        // requestDto로 받는 것 전에 User : 성능 면에서 좋지 않을 수 있다.

        // analyzing entities
        // sql exception handling -> try-catch로 각각의 처리 로직을 만들어 custom exception으로 처리를 한다!
        // 명확한 구분을 통해서 빠르게 분류하고 검사할 수 있다. 단, save 속도 저하 야기 가능

        // 중복 검증 로직을 한 번의 메소드 호출로 처리
        checkForDuplicate(requestDto.getUsername(), requestDto.getNickname(),
                requestDto.getEmail());

        User user = new User(
                requestDto.getUsername(),
                requestDto.getNickname(),
                requestDto.getPassword(), // 여기서 암호화 한 부분을 넣습니다.
                requestDto.getEmail(),
                requestDto.getInfo()
        );
        userRepository.save(user);

        // 명확한 exception 정의
    }

    private void checkForDuplicate(String username, String nickname, String email) {
        // 하나의 쿼리로 중복 여부 확인
        Optional<User> existingUser = userRepository.findByUsernameOrNicknameOrEmail(username,
                nickname, email);

        Map<String, String> errors = new HashMap<>();

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            if (username.equals(user.getUsername())) {
                errors.put("username", "Username already exists.");
            }
            if (nickname.equals(user.getNickname())) {
                errors.put("nickname", "Nickname already exists.");
            }
            if (email.equals(user.getEmail())) {
                errors.put("email", "Email already exists.");
            }
        }

        // 오류가 있을 경우 예외 발생
        if (!errors.isEmpty()) {
            throw new FieldValidationException(errors);
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

    // 이 부분은 토큰이 필요한 부분이다.
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
