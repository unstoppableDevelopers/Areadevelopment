package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.TokenDto;
import com.sparta.areadevelopment.dto.UserLoginRequestDto;
import com.sparta.areadevelopment.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/join")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {

        String username = userLoginRequestDto.getUsername();
        String password = userLoginRequestDto.getPassword();
        TokenDto token = authService.login(username,password);

        return ResponseEntity.ok(token);
    }
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }
}
