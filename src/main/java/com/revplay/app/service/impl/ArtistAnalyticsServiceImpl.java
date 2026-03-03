package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.ResourceNotFoundException;
import com.revplay.app.mapper.AlbumMapper;
import com.revplay.app.mapper.PodcastMapper;
import com.revplay.app.mapper.SongMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IArtistAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistAnalyticsServiceImpl implements IArtistAnalyticsService {
        private static final Logger log = LoggerFactory.getLogger(ArtistAnalyticsServiceImpl.class);

        private final IArtistProfileRepository artistProfileRepository;
        private final ISongRepository songRepository;
        private final IAlbumRepository albumRepository;
        private final IListeningHistoryRepository listeningHistoryRepository;
        private final IFavoriteRepository favoriteRepository;
        private final IPlaylistRepository playlistRepository;
        private final IPodcastRepository podcastRepository;
        private final IUserRepository userRepository;
        private final SongMapper songMapper;
        private final AlbumMapper albumMapper;
        private final PodcastMapper podcastMapper;

        @Override
        public ArtistDashboardResponse getDashboard(Long artistId) {
                log.info("Fetching dashboard for artistId: {}", artistId);
                ArtistProfile artist = artistProfileRepository.findById(artistId)
                                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", artistId));

                long totalSongs = songRepository.countByArtistIdAndIsDeleted(artistId, 0);
                long totalPlays = listeningHistoryRepository.countPlaysByArtistId(artistId);
                long totalFavorites = favoriteRepository.countFavoritesByArtistId(artistId);

                List<ArtistDashboardResponse.SongPlayCount> topSongs = getSongsByPopularity(artistId);
                List<ArtistDashboardResponse.TopListener> topListeners = getTopListeners(artistId, 10);

                List<Song> mySongs = songRepository.findByArtistIdAndIsDeletedNot(artistId, 1);
                List<Album> myAlbums = albumRepository.findByArtistId(artistId);
                List<Podcast> myPodcasts = podcastRepository.findByArtistId(artistId);

                return ArtistDashboardResponse.builder()
                                .artistProfileId(artistId)
                                .artistName(artist.getArtistName())
                                .totalSongs(totalSongs)
                                .totalPlays(totalPlays)
                                .totalFavorites(totalFavorites)
                                .topSongs(topSongs.size() > 10 ? topSongs.subList(0, 10) : topSongs)
                                .topListeners(topListeners)
                                .mySongs(mySongs.stream().map(songMapper::toResponse).collect(Collectors.toList()))
                                .myAlbums(myAlbums.stream().map(albumMapper::toResponse).collect(Collectors.toList()))
                                .myPodcasts(myPodcasts.stream().map(podcastMapper::toResponse)
                                                .collect(Collectors.toList()))
                                .build();
        }

        @Override
        public long getPlayCountForSong(Long songId) {
                return listeningHistoryRepository.countBySongId(songId);
        }

        @Override
        public List<ArtistDashboardResponse.SongPlayCount> getSongsByPopularity(Long artistId) {
                log.debug("Fetching songs by popularity for artistId: {}", artistId);
                List<Object[]> results = listeningHistoryRepository.countPlaysBySongForArtist(artistId);
                if (results.isEmpty())
                        return Collections.emptyList();

                List<Long> songIds = results.stream().map(row -> (Long) row[0]).collect(Collectors.toList());
                Map<Long, String> songTitles = songRepository.findAllById(songIds).stream()
                                .collect(Collectors.toMap(Song::getId, Song::getTitle));

                return results.stream().map(row -> {
                        Long songId = (Long) row[0];
                        Long playCount = (Long) row[1];
                        return ArtistDashboardResponse.SongPlayCount.builder()
                                        .songId(songId)
                                        .songTitle(songTitles.getOrDefault(songId, "Unknown"))
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
                log.debug("Fetching top {} listeners for artistId: {}", limit, artistId);
                List<Object[]> results = listeningHistoryRepository
                                .findTopListenersByArtistId(artistId, PageRequest.of(0, limit));
                if (results.isEmpty())
                        return Collections.emptyList();

                List<Long> userIds = results.stream().map(row -> (Long) row[0]).collect(Collectors.toList());
                Map<Long, String> usernames = userRepository.findAllById(userIds).stream()
                                .collect(Collectors.toMap(User::getId, User::getUsername));

                return results.stream().map(row -> {
                        Long userId = (Long) row[0];
                        Long playCount = (Long) row[1];
                        return ArtistDashboardResponse.TopListener.builder()
                                        .userId(userId)
                                        .username(usernames.getOrDefault(userId, "Unknown"))
                                        .playCount(playCount)
                                        .build();
                }).collect(Collectors.toList());
        }

        @Override
        public ArtistPageResponse getArtistPage(Long artistId) {
                log.info("Fetching public page for artistId: {}", artistId);
                ArtistProfile artist = artistProfileRepository.findById(artistId)
                                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", artistId));

                List<Song> songs = songRepository.findByArtistIdAndIsDeletedNot(artistId, 1);
                List<AlbumResponse> albumResponses = albumRepository.findByArtistId(artistId).stream()
                                .map(albumMapper::toResponse)
                                .collect(Collectors.toList());

                return ArtistPageResponse.builder()
                                .artistProfileId(artist.getId())
                                .artistName(artist.getArtistName())
                                .genreName(artist.getGenre() != null ? artist.getGenre().getName() : null)
                                .bannerImage(artist.getBannerImage())
                                .profileImage(artist.getUser() != null ? artist.getUser().getProfilePicture() : null)
                                .bio(artist.getUser() != null ? artist.getUser().getBio() : null)
                                .instagramLink(artist.getInstagramLink())
                                .youtubeLink(artist.getYoutubeLink())
                                .songs(songs.stream().map(songMapper::toResponse).collect(Collectors.toList()))
                                .albums(albumResponses)
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
                                .artistName(album.getArtist() != null ? album.getArtist().getArtistName() : "Unknown")
                                .artistId(album.getArtist() != null ? album.getArtist().getId() : null)
                                .tracks(tracks.stream().map(songMapper::toResponse).collect(Collectors.toList()))
                                .build();
        }

        @Override
        public UserStatsResponse getUserStats(Long userId) {
                log.info("Fetching user stats for userId: {}", userId);
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
