package com.sparta.areadevelopment.exception;

import com.sparta.areadevelopment.dto.ErrorResponseDto;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;


// AOP
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolations(DataIntegrityViolationException ex) {
        // 데이터베이스 에러 메시지를 분석하여 필요한 정보를 추출
        String errorMessage = "Data integrity violation occurred.";
        if (ex.getCause() != null && ex.getCause().getCause() != null) {
            String causeMessage = ex.getCause().getCause().getMessage();
            errorMessage = parseErrorMessage(causeMessage);
        }

        Map<String, String> response = new HashMap<>();
        response.put("error", "Data Integrity Violation");
        response.put("message", errorMessage);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // 데이터베이스 오류 메시지를 파싱하여 좀 더 친절한 메시지를 반환
    private String parseErrorMessage(String dbErrorMessage) {
        if (dbErrorMessage.contains("duplicate key value violates unique constraint")) {
            return "An entry with the same key already exists in the database.";
        }
        return dbErrorMessage; // 기본 메시지 반환
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
