package com.revplay.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "podcasts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Podcast {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "podcast_seq")
    @SequenceGenerator(name = "podcast_seq", sequenceName = "podcasts_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private ArtistProfile artist;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
