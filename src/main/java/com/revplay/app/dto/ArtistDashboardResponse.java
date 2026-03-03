package com.revplay.app.dto;

import java.util.List;

public class ArtistDashboardResponse {
    private Long artistProfileId;
    private String artistName;
    private long totalSongs;
    private long totalPlays;
    private long totalFavorites;
    private List<SongPlayCount> topSongs;
    private List<TopListener> topListeners;
    private List<SongResponse> mySongs;
    private List<AlbumResponse> myAlbums;
    private List<PodcastResponse> myPodcasts;

    public ArtistDashboardResponse() {
    }

    public static ArtistDashboardResponseBuilder builder() {
        return new ArtistDashboardResponseBuilder();
    }

    public Long getArtistProfileId() {
        return artistProfileId;
    }

    public void setArtistProfileId(Long artistProfileId) {
        this.artistProfileId = artistProfileId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getTotalSongs() {
        return totalSongs;
    }

    public void setTotalSongs(long totalSongs) {
        this.totalSongs = totalSongs;
    }

    public long getTotalPlays() {
        return totalPlays;
    }

    public void setTotalPlays(long totalPlays) {
        this.totalPlays = totalPlays;
    }

    public long getTotalFavorites() {
        return totalFavorites;
    }

    public void setTotalFavorites(long totalFavorites) {
        this.totalFavorites = totalFavorites;
    }

    public List<SongPlayCount> getTopSongs() {
        return topSongs;
    }

    public void setTopSongs(List<SongPlayCount> topSongs) {
        this.topSongs = topSongs;
    }

    public List<TopListener> getTopListeners() {
        return topListeners;
    }

    public void setTopListeners(List<TopListener> topListeners) {
        this.topListeners = topListeners;
    }

    public List<SongResponse> getMySongs() {
        return mySongs;
    }

    public void setMySongs(List<SongResponse> mySongs) {
        this.mySongs = mySongs;
    }

    public List<AlbumResponse> getMyAlbums() {
        return myAlbums;
    }

    public void setMyAlbums(List<AlbumResponse> myAlbums) {
        this.myAlbums = myAlbums;
    }

    public List<PodcastResponse> getMyPodcasts() {
        return myPodcasts;
    }

    public void setMyPodcasts(List<PodcastResponse> myPodcasts) {
        this.myPodcasts = myPodcasts;
    }

    public static class ArtistDashboardResponseBuilder {
        private ArtistDashboardResponse response = new ArtistDashboardResponse();

        public ArtistDashboardResponseBuilder artistProfileId(Long id) {
            response.setArtistProfileId(id);
            return this;
        }

        public ArtistDashboardResponseBuilder artistName(String name) {
            response.setArtistName(name);
            return this;
        }

        public ArtistDashboardResponseBuilder totalSongs(long count) {
            response.setTotalSongs(count);
            return this;
        }

        public ArtistDashboardResponseBuilder totalPlays(long count) {
            response.setTotalPlays(count);
            return this;
        }

        public ArtistDashboardResponseBuilder totalFavorites(long count) {
            response.setTotalFavorites(count);
            return this;
        }

        public ArtistDashboardResponseBuilder topSongs(List<SongPlayCount> songs) {
            response.setTopSongs(songs);
            return this;
        }

        public ArtistDashboardResponseBuilder topListeners(List<TopListener> listeners) {
            response.setTopListeners(listeners);
            return this;
        }

        public ArtistDashboardResponseBuilder mySongs(List<SongResponse> songs) {
            response.setMySongs(songs);
            return this;
        }

        public ArtistDashboardResponseBuilder myAlbums(List<AlbumResponse> albums) {
            response.setMyAlbums(albums);
            return this;
        }

        public ArtistDashboardResponseBuilder myPodcasts(List<PodcastResponse> podcasts) {
            response.setMyPodcasts(podcasts);
            return this;
        }

        public ArtistDashboardResponse build() {
            return response;
        }
    }

    public static class SongPlayCount {
        private Long songId;
        private String songTitle;
        private long playCount;

        public SongPlayCount() {
        }

        public static SongPlayCountBuilder builder() {
            return new SongPlayCountBuilder();
        }

        public Long getSongId() {
            return songId;
        }

        public void setSongId(Long songId) {
            this.songId = songId;
        }

        public String getSongTitle() {
            return songTitle;
        }

        public void setSongTitle(String songTitle) {
            this.songTitle = songTitle;
        }

        public long getPlayCount() {
            return playCount;
        }

        public void setPlayCount(long playCount) {
            this.playCount = playCount;
        }

        public static class SongPlayCountBuilder {
            private SongPlayCount count = new SongPlayCount();

            public SongPlayCountBuilder songId(Long id) {
                count.setSongId(id);
                return this;
            }

            public SongPlayCountBuilder songTitle(String title) {
                count.setSongTitle(title);
                return this;
            }

            public SongPlayCountBuilder playCount(long count) {
                this.count.setPlayCount(count);
                return this;
            }

            public SongPlayCount build() {
                return count;
            }
        }
    }

    public static class TopListener {
        private Long userId;
        private String username;
        private long playCount;

        public TopListener() {
        }

        public static TopListenerBuilder builder() {
            return new TopListenerBuilder();
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public long getPlayCount() {
            return playCount;
        }

        public void setPlayCount(long playCount) {
            this.playCount = playCount;
        }

        public static class TopListenerBuilder {
            private TopListener listener = new TopListener();

            public TopListenerBuilder userId(Long id) {
                listener.setUserId(id);
                return this;
            }

            public TopListenerBuilder username(String name) {
                listener.setUsername(name);
                return this;
            }

            public TopListenerBuilder playCount(long count) {
                listener.setPlayCount(count);
                return this;
            }

            public TopListener build() {
                return listener;
            }
        }
    }
}
