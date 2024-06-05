package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.SignOutRequestDto;
import com.sparta.areadevelopment.dto.SignupRequestDto;
import com.sparta.areadevelopment.dto.SignupResponseDto;
import com.sparta.areadevelopment.dto.SingOutResponseDto;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<SignupResponseDto> register(@RequestBody @Valid SignupRequestDto requestDto) {
        try{
            userService.signUp(requestDto);
            SignupResponseDto signupResponseDto = new SignupResponseDto("Successfully Signed Up");
            return new ResponseEntity<>(signupResponseDto, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(new SignupResponseDto(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{userId}/sign-out")
    public ResponseEntity<SingOutResponseDto> login(@RequestBody SignOutRequestDto requestDto) {
        try{
            User user = userService.signOut(requestDto);
            String token = JwtTokenProvider.generateToken(user.getUsername());
            AuthResponse authResponse = new AuthResponse("Successfully Log in", token);
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(new AuthResponse("Invalid username or password", null), HttpStatus.UNAUTHORIZED);
        }
    }
}

