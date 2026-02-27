package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Playlist name is required")
    @Size(max = 200)
    private String name;

    private String description;

    @Size(max = 500)
    private String coverImage;

    @Size(max = 20)
    private String privacy;
}
