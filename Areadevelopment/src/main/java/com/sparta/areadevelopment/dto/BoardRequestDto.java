package com.sparta.areadevelopment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    @NotBlank(message = "내용을 입력해주세요.")
    String content;


}
