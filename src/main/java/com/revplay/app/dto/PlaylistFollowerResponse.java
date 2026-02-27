package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistFollowerResponse {
    private Long id;
    private Long playlistId;
    private String playlistName;
    private Long userId;
    private String username;
    private LocalDateTime followedAt;
}
