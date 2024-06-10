package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.CommonResponse;
import com.sparta.areadevelopment.dto.PasswordChangeRequestDto;
import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.dto.UpdateUserDto;
import com.sparta.areadevelopment.dto.UserInfoDto;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity signUp(
            @Valid @RequestBody SignupRequestDto requestDto) {
        Long userId = userService.signUp(requestDto);
        return ResponseEntity.ok(
                new CommonResponse(HttpStatus.OK.value(), "Success to create User : " + userId));
    }


    @GetMapping("/{userId}")
    public ResponseEntity getUser(
            @PathVariable("userId") Long userId) {

        return ResponseEntity.ok(
                new CommonResponse<>(HttpStatus.OK.value(), "Success to get User : " + userId,
                        userService.getUser(userId)));
    }


    @PatchMapping("/{userId}")
    public ResponseEntity updateProfile(
            @PathVariable("userId") Long userId,
            @RequestBody UpdateUserDto requestDto) {

            userService.updateProfile(userId, requestDto);

        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), "Success to update User : " + userId));
    }

    // redirect status code 가 있다.
    @PutMapping("/{userId}/change-password")
    public ResponseEntity changePassword(@PathVariable("userId") Long userId, @RequestBody PasswordChangeRequestDto requestDto){
        userService.updatePassword(userId, requestDto);
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), "Success to update Password of user " + userId));
    }


    @PostMapping("/{userId}/sign-out")
    public ResponseEntity<CommonResponse> signOut(
            @PathVariable(name = "userId") Long userId
            , @RequestBody SignOutRequestDto requestDto) {
        userService.signOut(userId, requestDto);
        // token 검증
        return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), "Success to sign out"));
    }
}

