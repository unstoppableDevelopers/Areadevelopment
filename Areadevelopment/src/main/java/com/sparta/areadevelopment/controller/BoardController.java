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

/**
 * 뉴스피드 컨트롤러
 * 조회를 제외하고는 모두 User의 정보가 필요하다.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BoardController {

    /**
     * 보드 서비스
     */
    private final BoardService boardService;


    /**
     * 보드 생성 controller
     * @param userDetails
     * @param requestDto
     * @return
     */
    @PostMapping("/boards")
    public BoardResponseDto createBoard(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(userDetails.getUsername(), requestDto);
    }

    /**
     * 뉴스피드 내용 불러오기
     * @return
     * @throws ServiceNotFoundException
     */
    @GetMapping("/boards")
    public List<BoardResponseDto> findAll() throws ServiceNotFoundException {
        return boardService.findAll();
    }
    /**
     *  뉴스피드 검색
     */
    @GetMapping("/boards/{boardId}")
    public BoardResponseDto findBoard(@PathVariable Long boardId) {
        return boardService.findBoard(boardId);
    }

    /**
     * 뉴스피드 수정
     * @param userDetails
     * @param requestDto
     * @param boardId
     * @return
     */
    @PutMapping("/boards/{boardId}")
    public BoardResponseDto updateBoard(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BoardRequestDto requestDto,
            @PathVariable Long boardId) {

        return boardService.updateBoard(userDetails.getUsername(), requestDto, boardId);
    }

    /**
     * 뉴스피드 삭제
     * @param userDetails
     * @param boardId
     * @return
     */
    @DeleteMapping("/boards/{boardId}")
    public BoardResponseDto deleteBoard(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long boardId) {
        return boardService.deleteBoard(userDetails.getUsername(), boardId);
    }
}
