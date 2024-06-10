package com.sparta.areadevelopment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonResponse<T> {
	private Integer statsCode;
	private String msg;
	private T data;
}
