package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.TokenDto;
import com.sparta.areadevelopment.dto.UserLoginRequestDto;
import com.sparta.areadevelopment.entity.RefreshToken;
import com.sparta.areadevelopment.jwt.TokenProvider;
import com.sparta.areadevelopment.repository.RefreshTokenRepository;
import com.sparta.areadevelopment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public UserLoginRequestDto signup(UserLoginRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        return null;
    }

    @Transactional
    public TokenDto login(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    // Access Token 리프레시
    public TokenDto reissue(String refreshToken , String accessToken) {
        if (refreshTokenRepository.findByValue(refreshToken).equals(refreshToken)) {
            tokenProvider.validateToken(accessToken);
        }
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken originRefreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
        if (!originRefreshToken.getValue().equals(refreshToken)) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }
        TokenDto tokenDto = tokenProvider.generateToken(authentication);
        RefreshToken newRefreshToken = originRefreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }
}

