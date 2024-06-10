package com.sparta.areadevelopment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserDto {

    @NotBlank(message = "Required Nickname")
    private String nickname;
    @Email
    private String email;
    private String info;
    private String password;

}

