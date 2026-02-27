package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.ResourceNotFoundException;
import com.revplay.app.mapper.AlbumMapper;
import com.revplay.app.mapper.SongMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.ArtistAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistAnalyticsServiceImpl implements ArtistAnalyticsService {

        private final ArtistProfileRepository artistProfileRepository;
        private final SongRepository songRepository;
        private final AlbumRepository albumRepository;
        private final ListeningHistoryRepository listeningHistoryRepository;
        private final FavoriteRepository favoriteRepository;
        private final PlaylistRepository playlistRepository;
        private final UserRepository userRepository;
        private final SongMapper songMapper;
        private final AlbumMapper albumMapper;

        @Override
        public ArtistDashboardResponse getDashboard(Long artistId) {
                ArtistProfile artist = artistProfileRepository.findById(artistId)
                                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", artistId));

                long totalSongs = songRepository.countByArtistIdAndIsDeleted(artistId, 0);
                long totalPlays = listeningHistoryRepository.countPlaysByArtistId(artistId);
                long totalFavorites = favoriteRepository.countFavoritesByArtistId(artistId);

                List<ArtistDashboardResponse.SongPlayCount> topSongs = getSongsByPopularity(artistId);
                List<ArtistDashboardResponse.TopListener> topListeners = getTopListeners(artistId, 10);

                return ArtistDashboardResponse.builder()
                                .artistProfileId(artistId)
                                .artistName(artist.getArtistName())
                                .totalSongs(totalSongs)
                                .totalPlays(totalPlays)
                                .totalFavorites(totalFavorites)
                                .topSongs(topSongs.size() > 10 ? topSongs.subList(0, 10) : topSongs)
                                .topListeners(topListeners)
                                .build();
        }

        @Override
        public long getPlayCountForSong(Long songId) {
                return listeningHistoryRepository.countBySongId(songId);
        }

        @Override
        public List<ArtistDashboardResponse.SongPlayCount> getSongsByPopularity(Long artistId) {
                List<Object[]> results = listeningHistoryRepository.countPlaysBySongForArtist(artistId);
                return results.stream().map(row -> {
                        Long songId = (Long) row[0];
                        Long playCount = (Long) row[1];
                        String songTitle = songRepository.findById(songId)
                                        .map(Song::getTitle).orElse("Unknown");
                        return ArtistDashboardResponse.SongPlayCount.builder()
                                        .songId(songId)
                                        .songTitle(songTitle)
                                        .playCount(playCount)
                                        .build();
                }).collect(Collectors.toList());
        }

        @Override
        public List<Map<String, Object>> getUsersWhoFavoritedArtistSongs(Long artistId) {
                List<Favorite> favorites = favoriteRepository.findFavoritesByArtistSongs(artistId);
                return favorites.stream().map(fav -> {
                        Map<String, Object> entry = new LinkedHashMap<>();
                        entry.put("userId", fav.getUser().getId());
                        entry.put("username", fav.getUser().getUsername());
                        entry.put("songId", fav.getSong().getId());
                        entry.put("songTitle", fav.getSong().getTitle());
                        entry.put("favoritedAt", fav.getAddedAt());
                        return entry;
                }).collect(Collectors.toList());
        }

        @Override
        public List<Map<String, Object>> getDailyPlayTrends(Long artistId,
                        LocalDateTime startDate,
                        LocalDateTime endDate) {
                List<Object[]> results = listeningHistoryRepository
                                .findDailyPlaysByArtistId(artistId, startDate, endDate);
                return results.stream().map(row -> {
                        Map<String, Object> entry = new LinkedHashMap<>();
                        entry.put("date", row[0].toString());
                        entry.put("playCount", row[1]);
                        return entry;
                }).collect(Collectors.toList());
        }

        @Override
        public List<ArtistDashboardResponse.TopListener> getTopListeners(Long artistId, int limit) {
                List<Object[]> results = listeningHistoryRepository
                                .findTopListenersByArtistId(artistId, PageRequest.of(0, limit));
                return results.stream().map(row -> {
                        Long userId = (Long) row[0];
                        Long playCount = (Long) row[1];
                        String username = userRepository.findById(userId)
                                        .map(User::getUsername).orElse("Unknown");
                        return ArtistDashboardResponse.TopListener.builder()
                                        .userId(userId)
                                        .username(username)
                                        .playCount(playCount)
                                        .build();
                }).collect(Collectors.toList());
        }

        @Override
        public ArtistPageResponse getArtistPage(Long artistId) {
                ArtistProfile artist = artistProfileRepository.findById(artistId)
                                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", artistId));

                List<Song> songs = songRepository.findByArtistIdAndIsDeletedNot(artistId, 1);
                List<Album> albums = albumRepository.findByArtistId(artistId);

                return ArtistPageResponse.builder()
                                .artistProfileId(artist.getId())
                                .artistName(artist.getArtistName())
                                .genreName(artist.getGenre() != null ? artist.getGenre().getName() : null)
                                .bannerImage(artist.getBannerImage())
                                .instagramLink(artist.getInstagramLink())
                                .twitterLink(artist.getTwitterLink())
                                .youtubeLink(artist.getYoutubeLink())
                                .spotifyLink(artist.getSpotifyLink())
                                .websiteLink(artist.getWebsiteLink())
                                .songs(songs.stream().map(songMapper::toResponse).collect(Collectors.toList()))
                                .albums(albums.stream().map(albumMapper::toResponse).collect(Collectors.toList()))
                                .build();
        }

        @Override
        public AlbumDetailResponse getAlbumDetail(Long albumId) {
                Album album = albumRepository.findById(albumId)
                                .orElseThrow(() -> new ResourceNotFoundException("Album", albumId));

                List<Song> tracks = songRepository.findByAlbumId(albumId);

                return AlbumDetailResponse.builder()
                                .albumId(album.getId())
                                .albumName(album.getName())
                                .description(album.getDescription())
                                .coverImageUrl(album.getCoverImage())
                                .releaseDate(album.getReleaseDate() != null ? album.getReleaseDate().toString() : null)
                                .artistName(album.getArtist().getArtistName())
                                .artistId(album.getArtist().getId())
                                .tracks(tracks.stream().map(songMapper::toResponse).collect(Collectors.toList()))
                                .build();
        }

        @Override
        public UserStatsResponse getUserStats(Long userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

                long totalPlaylists = playlistRepository.countByUserIdAndIsDeleted(userId, 0);
                long totalFavorites = favoriteRepository.countByUserId(userId);
                long totalListening = listeningHistoryRepository.countByUserId(userId);

                return UserStatsResponse.builder()
                                .userId(userId)
                                .username(user.getUsername())
                                .totalPlaylists(totalPlaylists)
                                .totalFavorites(totalFavorites)
                                .totalListeningCount(totalListening)
                                .build();
        }
}
