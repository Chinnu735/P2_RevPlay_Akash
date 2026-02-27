package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongRequest {
    @NotNull(message = "Artist ID is required")
    private Long artistId;

    private Long albumId;
    private Long genreId;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    @NotBlank(message = "Audio URL is required")
    @Size(max = 500)
    private String audioUrl;

    private Long fileSize;
    private LocalDate releaseDate;

    @Size(max = 500)
    private String coverImage;

    @Size(max = 20)
    private String visibility;

    @Size(max = 20)
    private String status;
}
