package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.BoardRequestDto;
import com.sparta.areadevelopment.dto.BoardResponseDto;
import com.sparta.areadevelopment.entity.CustomUserDetails;
import com.sparta.areadevelopment.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import javax.management.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {

    private final BoardService boardService;

    // 조회를 제외하고는 모두 User의 정보가 필요하다.
    @PostMapping("/boards")
    public BoardResponseDto createBoard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(userDetails.getUser(), requestDto);
    }


    @GetMapping("/boards")
    public List<BoardResponseDto> findAll() throws ServiceNotFoundException {
        return boardService.findAll();
    }

    @GetMapping("/boards/{boardId}")
    public BoardResponseDto findBoard(@PathVariable Long boardId) {
        return boardService.findBoard(boardId);
    }

    @PutMapping("/boards/{boardId}")
    public BoardResponseDto updateBoard(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BoardRequestDto requestDto,
            @PathVariable Long boardId) {

        return boardService.updateBoard(userDetails.getUsername(), requestDto, boardId);
    }

    @DeleteMapping("/boards/{boardId}")
    public BoardResponseDto deleteBoard(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long boardId) {
        return boardService.deleteBoard(userDetails.getUsername(), boardId);
    }
}
