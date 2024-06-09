package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.TokenDto;
import com.sparta.areadevelopment.dto.UserLoginRequestDto;
import com.sparta.areadevelopment.entity.RefreshToken;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.jwt.TokenProvider;
import com.sparta.areadevelopment.repository.UserRepository;
import java.util.Optional;
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

    @Transactional
    public TokenDto login(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        Optional<User> user=userRepository.findByUsername(username);
        user.get().updateValue(tokenDto.getRefreshToken());
        return tokenDto;
    }

    @Transactional
    // Access Token 리프레시
    public TokenDto reissue(String refreshToken) {
        if(!userRepository.findByRefreshToken(refreshToken).equals(refreshToken)){
            throw new RuntimeException("잘못된 토큰입니다.");
        }
        Optional<User> user = userRepository.findByRefreshToken(refreshToken);
        String username = user.get().getUsername();
        String password = user.get().getPassword();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);
        Authentication authentication =  authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);

//        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
//        User originRefreshToken = userRepository.findByUsername(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
//        if (!originRefreshToken.getRefreshToken().equals(refreshToken)) {
//            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
//        }
        TokenDto tokenDto = tokenProvider.generateToken(authentication);
        user.get().updateValue(tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Transactional
    public TokenDto logout(String accessToken, String refreshToken) {
        //  Refresh Token 검증
        if(!userRepository.findByRefreshToken(refreshToken).equals(refreshToken)){
            throw new RuntimeException("잘못된 토큰입니다.");
        }

        //  Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(accessToken);


        // 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        // 토큰 발급
        return tokenDto;
    }
}


