package com.sparta.areadevelopment.filter;

import com.sparta.areadevelopment.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;


import java.io.IOException;

@RequiredArgsConstructor
public class JwtLoginFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("username = " + username);
        System.out.println("password = " + password);
        System.out.println("request.getRequestURI() = " + request.getRequestURI());


        if(username != null && password  != null && (request.getRequestURI().equals("/api/auth/login"))){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 비밀번호 확인
            if(!bCryptPasswordEncoder.matches(password, userDetails.getPassword())) {
                throw new IllegalAccessError("비밀번호가 일치하지 않습니다.");
            }

            // 인증 객체 생성 및 등록
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            context.setAuthentication(authentication);

            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request,response);
    }
}