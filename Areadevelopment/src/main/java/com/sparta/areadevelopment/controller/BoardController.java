package com.sparta.areadevelopment.controller;

import com.sparta.areadevelopment.dto.BoardRequestDto;
import com.sparta.areadevelopment.dto.BoardResponseDto;
import com.sparta.areadevelopment.service.BoardService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.RequestParam;
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
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(userDetails.getUsername(), requestDto);
    }


    @GetMapping("/boards")
    public List<BoardResponseDto> findAll() throws ServiceNotFoundException {
        return boardService.findAll();
    }


    // 10개씩 페이지네이션하여, 각 페이지 당 뉴스피드 데이터가 10개씩 최신순으로 나오게 합니다.
    @GetMapping("/boards/recently/{page}")
    public List<BoardResponseDto> findAllRecentlyPagination(@PathVariable int page)
            throws ServiceNotFoundException {

        // ex) 1페이지 조회시 -> index는 0으로 들어가므로 -1을 해줌
        return boardService.findAllRecentlyPagination(page - 1);
    }

    // 좋아요 개수가 많은 순서대로 정렬 (페이지 당 뉴스피드 데이터 = 10개 고정)
    @GetMapping("/boards/like/{page}")
    public List<BoardResponseDto> findAllLikesPagination(@PathVariable int page)
            throws ServiceNotFoundException {

        return boardService.findAllLikesPagination(page - 1);
    }

    /**
     * 기간별 조회 ex ) String startTime = 2024-05-07 이런식으로 넣어서 테스트 합니다.
     */
    @GetMapping("/boards/date/{page}")
    public List<BoardResponseDto> findAllDatePagination(@PathVariable int page,
            @RequestParam String startTime,
            @RequestParam String endTime) throws ServiceNotFoundException {

        // LocalDate.parse를 사용하여 문자열을 LocalDate로 파싱
        LocalDate startDate = LocalDate.parse(startTime);
        LocalDate endDate = LocalDate.parse(endTime);

        // LocalDateTime으로 변환
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        return boardService.findAllDatePagination(page - 1, startDateTime, endDateTime);
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
