package com.sparta.areadevelopment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Length(min = 4, max = 10)
    @Column(unique = true, nullable = false)
    private String username;
    @Length(min = 8, max = 15)
    @Column(nullable = false)
    private String password;
    private Authority authority;

    public void setUserInfo(String username, String password, Authority authority ) {
        this.username = username;
        this.password = password;
        this.authority = authority;
    }
}
