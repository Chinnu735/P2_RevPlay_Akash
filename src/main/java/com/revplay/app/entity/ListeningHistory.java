package com.revplay.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "listening_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListeningHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "listening_history_seq")
    @SequenceGenerator(name = "listening_history_seq", sequenceName = "listening_history_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "played_at")
    private LocalDateTime playedAt;
}
