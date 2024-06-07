package com.sparta.areadevelopment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserDto {

    @NotBlank(message = "Required Nickname")
    private String nickname;
    @Email
    private String email;
    private String info;
    @NotBlank(message = "Required Password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{10,}$", message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다. \n비밀번호는 최소 10글자 이상이어야 합니다.")
    private String password;

    @Builder
    public UpdateUserDto(String nickname, String email, String info, String password) {
        this.nickname = nickname;
        this.email = email;
        this.info = info;
        this.password = password;
    }
}

