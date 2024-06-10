package com.sparta.areadevelopment.dto;

import com.sparta.areadevelopment.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserDto {

    @NotBlank(message = "Required Nickname")
    private String nickname;
    @Email
    private String email;
    private String info;
    private String password;

    @Builder
    public User toEntity(String nickname, String email, String info, String password){
        return User.builder()
                .nickname(nickname)
                .email(email)
                .info(info)
                .password(password)
                .build();
    }
}

