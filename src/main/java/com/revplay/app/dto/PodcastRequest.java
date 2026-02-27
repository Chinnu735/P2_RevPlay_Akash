package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PodcastRequest {
    @NotNull(message = "Artist ID is required")
    private Long artistId;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    private String title;

    private String description;

    @Size(max = 500)
    private String coverImage;
}
