package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.BoardRequestDto;
import com.sparta.areadevelopment.dto.BoardResponseDto;
import com.sparta.areadevelopment.entity.Board;
import com.sparta.areadevelopment.repository.BoardRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.management.ServiceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponseDto createBoard(BoardRequestDto requestDto) {
        Board board = boardRepository.save(new Board(requestDto));
        return new BoardResponseDto(board);
    }

    // 모든 페이지 조회, 글이 있을 경우 ApiResponseDto의 data 조회

    public List<BoardResponseDto> findAll() throws ServiceNotFoundException {
        List<BoardResponseDto> list = boardRepository.findAllByDeletedAtIsNullOrderByCreatedAtDesc()
                .stream()
                .map(BoardResponseDto::new).collect(Collectors.toList());

        // 글이 없을 경우 메시지와 200 Status code 던져줌
        if (list.isEmpty()) {
            throw new ServiceNotFoundException("먼저 작성하여 소식을 알려보세요!");
        }

        return list;
    }


    @Transactional
    public BoardResponseDto findById(Long boardId) {

        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId).orElseThrow(
                () -> new IllegalArgumentException("잘못된 요청입니다.")
        );

        // 조회수 + 1
        board.hitsUp();

        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto update(BoardRequestDto requestDto, Long boardId) {
        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId).orElseThrow(
                () -> new IllegalArgumentException("잘못된 요청입니다.")
        );

        board.update(requestDto);
        return new BoardResponseDto(board);
    }


    @Transactional
    public BoardResponseDto delete(Long boardId) {

        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId).orElseThrow(
                () -> new IllegalArgumentException("잘못된 요청입니다.")
        );

        // 삭제시간 저장
        board.setDeletedAt(board.getModifiedAt());

        log.info(board.getDeletedAt().toString());

        return new BoardResponseDto(board);
    }
}

