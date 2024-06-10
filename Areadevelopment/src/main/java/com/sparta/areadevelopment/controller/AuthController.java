package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.TokenDto;
import com.sparta.areadevelopment.dto.UserLoginDto;
import com.sparta.areadevelopment.enums.AuthEnum;
import com.sparta.areadevelopment.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginRequestDto, HttpServletResponse response) {

        String username = userLoginRequestDto.getUsername();
        String password = userLoginRequestDto.getPassword();
        TokenDto token = authService.login(username,password);
        response.setHeader(AuthEnum.ACCESS_TOKEN.getValue(), token.getAccessToken());
        response.setHeader(AuthEnum.REFRESH_TOKEN.getValue(), token.getRefreshToken());
        return ResponseEntity.ok("로그인 완료!");
    }
    @PostMapping("/reissue")
    public ResponseEntity<String> reissue(HttpServletRequest request,HttpServletResponse response) {
        String refreshToken = request.getHeader("refresh-token");
        TokenDto token = authService.reissue(refreshToken);
        response.setHeader(AuthEnum.ACCESS_TOKEN.getValue(), token.getAccessToken());
        response.setHeader(AuthEnum.REFRESH_TOKEN.getValue(), token.getRefreshToken());
        return ResponseEntity.ok("재발급완료");
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,HttpServletResponse response, Authentication authentication) {
        authService.logout(request, response, authentication);
        return ResponseEntity.ok("로그아웃완료");
    }

    @PostMapping("/send-mail")
    public ResponseEntity<String> sendMail(HttpServletRequest request){
        String refreshToken = request.getHeader("refresh-token");
        return authService.sendMail(refreshToken);
    }
    @PostMapping("/check-mail")
    public ResponseEntity<String> checkMail( HttpServletRequest request,@RequestBody String insertKey){
        String refreshToken = request.getHeader("refresh-token");
        return authService.checkMail(refreshToken, insertKey);
    }
}
