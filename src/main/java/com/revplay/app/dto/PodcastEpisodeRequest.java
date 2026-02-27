package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PodcastEpisodeRequest {
    @NotNull(message = "Podcast ID is required")
    private Long podcastId;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    private String description;

    @NotBlank(message = "Audio URL is required")
    @Size(max = 500)
    private String audioUrl;

    private Long duration;
    private LocalDate releaseDate;
}
