package com.sparta.areadevelopment.service;

import com.sparta.areadevelopment.dto.BoardRequestDto;
import com.sparta.areadevelopment.dto.BoardResponseDto;
import com.sparta.areadevelopment.entity.Board;
import com.sparta.areadevelopment.entity.User;
import com.sparta.areadevelopment.repository.BoardRepository;
import com.sparta.areadevelopment.repository.UserRepository;
import java.util.List;
import java.util.Objects;
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


    /**
     * 1. deletedAt = Null 인 경우에만 조회가 가능합니다. (삭제되는 순간 LocalDateTime.now()로 변경됩니다. 2.
     *
     * @AuthenticationPrincipal을 통해 User 정보를 받아 온 후 검증합니다. 3. board와 User은 다:1 , board와 comment는 1:다
     * 관계로 맺어줬습니다.
     */
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardResponseDto createBoard(String username, BoardRequestDto requestDto) {

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("로그인 한 상태에서만 작성 할 수 있습니다.")
        );

        Board board = boardRepository.save(new Board(user, requestDto));
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
    public BoardResponseDto findBoard(Long boardId) {

        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다.")
        );

        // 조회수 + 1
        board.hitsUp();

        return new BoardResponseDto(board);
    }

    @Transactional
    public BoardResponseDto updateBoard(String userName, BoardRequestDto requestDto, Long boardId) {
        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다.")
        );

        // 같은 사용자만 수정 가능
        if (!Objects.equals(board.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }

        board.update(requestDto);
        return new BoardResponseDto(board);
    }


    @Transactional
    public BoardResponseDto deleteBoard(String userName, Long boardId) {

        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        Board board = boardRepository.findByIdAndDeletedAtIsNull(boardId).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다.")
        );

        if (!Objects.equals(board.getUser().getId(), user.getId())) {
            throw new IllegalArgumentException("작성자만 삭제 가능합니다.");
        }

        // 삭제시간 저장
        board.setDeletedAt(board.getModifiedAt());
        log.info(board.getDeletedAt().toString());
        return new BoardResponseDto(board);
    }
}

