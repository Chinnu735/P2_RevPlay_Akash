package com.revplay.app.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "artist_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "artist_profile_seq")
    @SequenceGenerator(name = "artist_profile_seq", sequenceName = "artist_profiles_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "artist_name", length = 200, nullable = false)
    private String artistName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @Column(name = "banner_image", length = 500)
    private String bannerImage;

    @Column(name = "instagram_link", length = 255)
    private String instagramLink;

    @Column(name = "twitter_link", length = 255)
    private String twitterLink;

    @Column(name = "youtube_link", length = 255)
    private String youtubeLink;

    @Column(name = "spotify_link", length = 255)
    private String spotifyLink;

    @Column(name = "website_link", length = 255)
    private String websiteLink;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
