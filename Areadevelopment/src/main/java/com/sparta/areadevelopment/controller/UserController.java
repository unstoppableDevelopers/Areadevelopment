package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.dto.SignupResponseDto;
import com.sparta.areadevelopment.dto.UpdateUserDto;
import com.sparta.areadevelopment.dto.UserInfoDto;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.exception.GlobalExceptionHandler;
import com.sparta.areadevelopment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignupResponseDto> register(
            @RequestBody @Valid SignupRequestDto requestDto) throws Exception {
            userService.signUp(requestDto);
            SignupResponseDto signupResponseDto = new SignupResponseDto("Successfully Signed Up");
            return new ResponseEntity<>(signupResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoDto> getUserById(
            @PathVariable("userId") Long userId,
            @RequestBody SignupRequestDto requestDto) {
        return new ResponseEntity<>(userService.getUser(userId, requestDto.getPassword()),
                HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<SignupResponseDto> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody UpdateUserDto requestDto
    ) {
        userService.updateProfile(userId, requestDto);
        return new ResponseEntity<>(new SignupResponseDto("Successfully Updated User's profile"),
                HttpStatus.OK);
    }

    @PostMapping("/{userId}/sign-out")
    public ResponseEntity<User> signout(
            @PathVariable(name = "userId") Long userId
            , @RequestBody SignOutRequestDto requestDto) {
            User user = userService.signOut(userId, requestDto);
            // token 검증
            return ResponseEntity.ok().body(user);
    }
}

