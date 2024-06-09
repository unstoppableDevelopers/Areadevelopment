package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.TokenDto;
import com.sparta.areadevelopment.dto.UserLoginRequestDto;
import com.sparta.areadevelopment.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {

        String username = userLoginRequestDto.getUsername();
        String password = userLoginRequestDto.getPassword();
        TokenDto token = authService.login(username,password);
        response.setHeader("refresh-token", token.getRefreshToken());
        response.setHeader("access-token", token.getAccessToken());
        return ResponseEntity.ok("로그인 완료!");
    }
    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request,HttpServletResponse response) {
        String refreshToken = request.getHeader("refresh-token");
        TokenDto token = authService.reissue(refreshToken);
        response.setHeader("access-token", token.getAccessToken());
        response.setHeader("refresh-token", token.getRefreshToken());
        return ResponseEntity.ok("재발급완료");
    }
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,HttpServletResponse response) {
        String refreshToken = request.getHeader("refresh-token");
        String accessToken = request.getHeader("access-token");
        TokenDto token = authService.logout(accessToken, refreshToken);
        response.setHeader("refresh-token", token.getRefreshToken());
        return ResponseEntity.ok("로그아웃완료");
    }
}
