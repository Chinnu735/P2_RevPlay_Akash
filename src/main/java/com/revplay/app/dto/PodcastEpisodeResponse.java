package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PodcastEpisodeResponse {
    private Long id;
    private Long podcastId;
    private String podcastTitle;
    private String title;
    private String description;
    private String audioUrl;
    private Long duration;
    private LocalDate releaseDate;
    private LocalDateTime createdAt;
}
