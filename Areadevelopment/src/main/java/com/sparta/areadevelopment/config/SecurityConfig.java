package com.sparta.areadevelopment.config;

import com.sparta.areadevelopment.filter.JwtAuthenticationFilter;
import com.sparta.areadevelopment.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ComponentScan(basePackages = "com.sparta.areadevelopment.jwt")
public class SecurityConfig {
   private final TokenProvider tokenProvider;
    //암호화
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable);
        //폼을통한 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);
        //http basic 인증방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);
        //경로 별 인가
        http
                .authorizeHttpRequests((auth) ->auth
                                .requestMatchers("/auth/reissue","/auth/login").permitAll()
                                .requestMatchers(HttpMethod.GET).permitAll()
                                .anyRequest().authenticated()
                        );
        http
                .addFilterBefore(new JwtAuthenticationFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        //세션 jwt를 통해 인증 인가를 위해 stateless 상태 설정
        http
                .sessionManagement((session) ->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
