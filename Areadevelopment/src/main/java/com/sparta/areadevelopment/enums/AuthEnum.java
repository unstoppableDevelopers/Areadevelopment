package com.sparta.areadevelopment.enums;


public enum AuthEnum{
    ACCESS_TOKEN ("Authorization"),           // 30분
    REFRESH_TOKEN ("refresh-token"), // 2주
    GRANT_TYPE ("Bearer ");
    private String token;
    AuthEnum(String token) {
        this.token = token;
    }
    public String getValue(){
        return token;
    }
}
