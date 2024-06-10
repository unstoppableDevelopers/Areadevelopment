package com.sparta.areadevelopment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CommonResponse<T> {
	private Integer statusCode;
	private String msg;
	private T data;

	public CommonResponse(int statusCode, String message) {
		this(statusCode, message, null);  // 데이터 없이 응답을 생성할 수 있는 생성자
	}

	public CommonResponse(int statusCode, String message, T data) {
		this.statusCode = statusCode;
		this.msg = message;
		this.data = data;
	}
}
