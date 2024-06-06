package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.TokenDto;
import com.sparta.areadevelopment.dto.UserLoginRequestDto;
import com.sparta.areadevelopment.jwt.TokenProvider;
import com.sparta.areadevelopment.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {

        String username = userLoginRequestDto.getUsername();
        String password = userLoginRequestDto.getPassword();
        TokenDto token = authService.login(username,password);
        Cookie refreshToken = new Cookie("refreshToken", token.getRefreshToken());
        response.setHeader("Set-Cookie", token.getRefreshToken());
        return ResponseEntity.ok(token);
    }
    @PostMapping("/reissue")
    public ResponseEntity<Object> reissue(HttpServletRequest request,HttpServletResponse response) {
        Cookie refreshToken = WebUtils.getCookie(request, "refreshToken");
        Object responseJson = authService.reissue(refreshToken.getValue());
        if (responseJson instanceof TokenDto) {
            TokenDto tokenDto = (TokenDto) responseJson;
            response.setHeader("Set-Cookie", tokenDto.getRefreshToken());
        }
        return ResponseEntity.ok(responseJson);
    }

}
