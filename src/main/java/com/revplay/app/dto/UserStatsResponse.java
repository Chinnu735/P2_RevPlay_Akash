package com.revplay.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatsResponse {
    private Long userId;
    private String username;
    private long totalPlaylists;
    private long totalFavorites;
    private long totalListeningCount;
}
