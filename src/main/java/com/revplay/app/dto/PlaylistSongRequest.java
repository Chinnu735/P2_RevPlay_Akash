package com.revplay.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSongRequest {
    @NotNull(message = "Playlist ID is required")
    private Long playlistId;

    @NotNull(message = "Song ID is required")
    private Long songId;

    private Integer orderIndex;
}
