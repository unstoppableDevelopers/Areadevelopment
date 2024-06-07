package com.sparta.areadevelopment.dto;

import lombok.Getter;

@Getter
public class UserInfoDto {

    private Long id;
    private String nickname;
    private String info;
    private String email;

    public UserInfoDto(Long id, String nickname, String info, String email) {
        this.id = id;
        this.nickname = nickname;
        this.info = info;
        this.email = email;
    }
}
