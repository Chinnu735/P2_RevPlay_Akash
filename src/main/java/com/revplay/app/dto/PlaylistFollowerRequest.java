package com.revplay.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistFollowerRequest {
    @NotNull(message = "Playlist ID is required")
    private Long playlistId;

    @NotNull(message = "User ID is required")
    private Long userId;
}
