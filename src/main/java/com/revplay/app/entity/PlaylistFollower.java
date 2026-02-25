package com.revplay.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "playlist_followers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistFollower {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "playlist_follower_seq")
    @SequenceGenerator(name = "playlist_follower_seq", sequenceName = "playlist_followers_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "followed_at")
    private LocalDateTime followedAt;
}
