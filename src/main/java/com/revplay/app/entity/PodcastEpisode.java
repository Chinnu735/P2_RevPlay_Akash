package com.revplay.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "podcast_episodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PodcastEpisode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "podcast_episode_seq")
    @SequenceGenerator(name = "podcast_episode_seq", sequenceName = "podcast_episodes_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "podcast_id", nullable = false)
    private Podcast podcast;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "audio_url", length = 500, nullable = false)
    private String audioUrl;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
