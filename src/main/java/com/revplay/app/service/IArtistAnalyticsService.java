package com.revplay.app.service;

import com.revplay.app.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IArtistAnalyticsService {
    ArtistDashboardResponse getDashboard(Long artistId);

    long getPlayCountForSong(Long songId);

    List<ArtistDashboardResponse.SongPlayCount> getSongsByPopularity(Long artistId);

    List<Map<String, Object>> getUsersWhoFavoritedArtistSongs(Long artistId);

    List<Map<String, Object>> getDailyPlayTrends(Long artistId, LocalDateTime startDate, LocalDateTime endDate);

    List<ArtistDashboardResponse.TopListener> getTopListeners(Long artistId, int limit);

    ArtistPageResponse getArtistPage(Long artistId);

    AlbumDetailResponse getAlbumDetail(Long albumId);

    UserStatsResponse getUserStats(Long userId);
}
