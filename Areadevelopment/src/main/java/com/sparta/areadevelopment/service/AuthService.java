package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.config.MailManager;
import com.sparta.areadevelopment.dto.TokenDto;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.enums.AuthEnum;
import com.sparta.areadevelopment.enums.StatusEnum;
import com.sparta.areadevelopment.jwt.TokenProvider;
import com.sparta.areadevelopment.repository.UserRepository;
import com.sparta.areadevelopment.util.SHA256Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService implements LogoutHandler {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MailManager mailManager;

    @Transactional
    public TokenDto login(String username, String password) {
        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException(username);
        }
        Optional<User> user = userRepository.findUserByUsernameAndStatus(username, StatusEnum.ACTIVE);
        bCryptPasswordEncoder.matches(password, user.get().getPassword());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,password);

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        TokenDto tokenDto = tokenProvider.generateToken(authentication);
        // 인증 객체 생성 및 등록


        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        user.get().updateToken(tokenDto.getRefreshToken());
        user.get().setExpired(false);
        return tokenDto;
    }

    @Transactional
    // Access Token 리프레시
    public TokenDto reissue(String refreshToken) {
        Optional<User> user = userRepository.findByRefreshToken(refreshToken);
        if(user!=null && !user.get().getRefreshToken().equals(refreshToken)){
            throw new RuntimeException("잘못된 토큰입니다.");
        }else if(user.get().isExpired()){
            throw new RuntimeException("폐지된 토큰입니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(refreshToken.substring(7));
//        String resolveToken = resolveToken(user.get().getRefreshToken());


        TokenDto tokenDto = tokenProvider.generateToken(authentication);
        user.get().updateToken(tokenDto.getRefreshToken());

        return tokenDto;
    }

    @Transactional
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response , Authentication authentication) {
        String authHeader = request.getHeader(AuthEnum.ACCESS_TOKEN.getValue());

        if (authHeader == null && !authHeader.startsWith(AuthEnum.GRANT_TYPE.getValue())) {
            throw new RuntimeException("알수 없는 access token.");
        }
        String accessToken = authHeader.substring(7);
        String username = tokenProvider.getUsername(accessToken);
        User refreshToken = userRepository.findByUsername(username).orElse(null);

        refreshToken.setExpired(true);

    }

    public ResponseEntity<String> sendMail(String token){
        String userEmail = userRepository.findByRefreshToken(token).get().getEmail();

        UUID uuid = UUID.randomUUID();
        String key = uuid.toString().substring(0,7);
        String sub = "인증번호 메일 전송";
        String con = "인증번호 : " + key;
        try {
            mailManager.send(userEmail,sub,con);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        key = SHA256Util.getEncrypt(key, userEmail);
        return ResponseEntity.ok(key);
    }

    public ResponseEntity<String> checkMail(String token, String insertKey){
        if(userRepository.findByRefreshToken(token).isPresent() && !userRepository.findByRefreshToken(token).get().getRefreshToken().equals(token)){
            new RuntimeException("잘못된 토큰입니다.");
        }
        String userEmail = userRepository.findByRefreshToken(token).get().getEmail();
        insertKey = SHA256Util.getEncrypt(insertKey, userEmail);

        return null;
    }
}


