package com.revplay.app.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistDashboardResponse {
    private Long artistProfileId;
    private String artistName;
    private long totalSongs;
    private long totalPlays;
    private long totalFavorites;
    private List<SongPlayCount> topSongs;
    private List<TopListener> topListeners;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SongPlayCount {
        private Long songId;
        private String songTitle;
        private long playCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TopListener {
        private Long userId;
        private String username;
        private long playCount;
    }
}
