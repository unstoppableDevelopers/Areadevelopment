package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.CommonResponse;
import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.dto.SignupResponseDto;
import com.sparta.areadevelopment.dto.UpdateUserDto;
import com.sparta.areadevelopment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<SignupResponseDto> signUp(
            @Valid @RequestBody SignupRequestDto requestDto) {
        Long userId = userService.signUp(requestDto);
        SignupResponseDto signupResponseDto = new SignupResponseDto(
                "Successfully Signed Up with ID : " + userId);
        return new ResponseEntity<>(signupResponseDto, HttpStatus.OK);
    }


    @PostMapping("/{userId}")
    public ResponseEntity<CommonResponse<Object>> getUser(
            @PathVariable("userId") Long userId) {

        return ResponseEntity.ok().body(CommonResponse.builder()
                .statsCode(HttpStatus.OK.value())
                .msg("Success to search this user : " + userId)
                .data(userService.getUser(userId)).build()
        );
    }


    @PatchMapping("/{userId}")
    public ResponseEntity<CommonResponse> updateProfile(
            @PathVariable("userId") Long userId,
            @RequestBody UpdateUserDto requestDto) {
        userService.updateProfile(userId, requestDto);
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statsCode(HttpStatus.OK.value())
                .msg("Success to update user profile").build()
        );

    }

    @PostMapping("/{userId}/sign-out")
    public ResponseEntity<CommonResponse> signOut(
            @PathVariable(name = "userId") Long userId
            , @RequestBody SignOutRequestDto requestDto) {
        userService.signOut(userId, requestDto);
        // token 검증
        return ResponseEntity.ok().body(CommonResponse.builder()
                .statsCode(HttpStatus.OK.value())
                .msg("Success to delete user").build()
        );
    }
}

