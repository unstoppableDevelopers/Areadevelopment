package com.sparta.areadevelopment.entity;


import com.sparta.areadevelopment.dto.BoardRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Table(name = "boards")
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Board {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long hits;

    @Column(nullable = false)
    private Long likeCount;

    @CreatedDate
    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @CreatedDate
    @Column
    private LocalDateTime modifiedAt;

    @Column
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Board(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.hits = 0L;
        this.likeCount = 0L;
        this.modifiedAt = LocalDateTime.now();
        this.deletedAt = null;
    }

    // 조회수 증가
    public void hitsUp() {
        this.hits++;
    }

    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.modifiedAt = LocalDateTime.now();
}
}

    

