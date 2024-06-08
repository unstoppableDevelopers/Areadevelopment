package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.BoardRequestDto;
import com.sparta.areadevelopment.dto.BoardResponseDto;
import com.sparta.areadevelopment.entity.CustomUserDetails;
import com.sparta.areadevelopment.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import javax.management.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public BoardResponseDto findById(@PathVariable Long boardId) {
        return boardService.findById(boardId);
    }

    @PutMapping("/boards/{boardId}")
    public BoardResponseDto update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody BoardRequestDto requestDto,
            @PathVariable Long boardId) {

        return boardService.update(userDetails.getUser(), requestDto, boardId);
    }

    @DeleteMapping("/boards/{boardId}")
    public BoardResponseDto delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long boardId) {
        return boardService.delete(userDetails.getUser(), boardId);
    }
}
