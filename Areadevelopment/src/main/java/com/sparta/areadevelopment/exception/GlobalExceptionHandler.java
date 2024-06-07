package com.sparta.areadevelopment.exception;

import com.sparta.areadevelopment.dto.ErrorResponseDto;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


// AOP
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FieldValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleFieldValidationException(
            FieldValidationException ex) {
        List<String> errorMessages =
                ex.getErrors()
                        .values() // Map errors에서 입력한 오류 메세지들을 가져온다.
                        .stream()// stream 으로 나열한다.
                        .collect(Collectors.collectingAndThen(
                                // Collectors로 받고 Collections을 사용해서 역순 정렬 후 리턴
                                Collectors.toList(),
                                list -> {
                                    Collections.reverse(list);
                                    return list;
                                }));

        ErrorResponseDto errorResponse = new ErrorResponseDto(ex.getMessage(), errorMessages);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 해당 유저 찾기 로직
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // 잘못된 URL 입력에 의한 처리
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return new ResponseEntity<>(
                "The requested URL was not found on the server. If you entered the URL manually please check your spelling and try again.",
                HttpStatus.BAD_REQUEST);
    }

}
