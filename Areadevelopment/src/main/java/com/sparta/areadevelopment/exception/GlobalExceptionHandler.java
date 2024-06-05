package com.sparta.areadevelopment.exception;

import jakarta.transaction.NotSupportedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 서비스에서 지원하지 않는 기능을 요청했을때 발생합니다.
     *
     * @param e 익셉션의 발생시 작성한 오류 메세지
     * @return 상태코드 400을 보내고 바디에 오류메세지를 보냅니다.
     */
    @ExceptionHandler(NotSupportedException.class)
    public ResponseEntity<Object> handleNotSupportedException(NotSupportedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * 메서드에 전달된 매개변수 값이 잘못되어 찾을 수 없을때 발생합니다.
     *
     * @param e 익셉션의 발생시 작성한 오류 메세지
     * @return 상태코드 400을 보내고 바디에 오류메세지를 보냅니다.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
