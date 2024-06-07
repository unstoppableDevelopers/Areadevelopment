package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.BoardRequestDto;
import com.sparta.areadevelopment.dto.BoardResponseDto;
import com.sparta.areadevelopment.service.BoardService;
import jakarta.validation.Valid;
import java.util.List;
import javax.management.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
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

    //TODO 공통 파라미터 User 추가
    @PostMapping("/boards")
    public BoardResponseDto createBoard(@Valid @RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
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
    public BoardResponseDto update(@Valid @RequestBody BoardRequestDto requestDto,
            @PathVariable Long boardId) {

        return boardService.update(requestDto, boardId);
    }

    @DeleteMapping("/boards/{boardId}")
    public BoardResponseDto delete(@PathVariable Long boardId) {
        return boardService.delete(boardId);
    }
}
