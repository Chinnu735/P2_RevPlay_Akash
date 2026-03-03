package com.revplay.app.dto;

import java.time.LocalDateTime;

public class PlaylistSongResponse {
    private Long id;
    private Long playlistId;
    private Long songId;
    private String songTitle;
    private String artistName;
    private String coverImage;
    private Integer orderIndex;
    private LocalDateTime addedAt;
    private Integer duration;

    public PlaylistSongResponse() {
    }

    public static PlaylistSongResponseBuilder builder() {
        return new PlaylistSongResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
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

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public static class PlaylistSongResponseBuilder {
        private PlaylistSongResponse response = new PlaylistSongResponse();

        public PlaylistSongResponseBuilder id(Long id) {
            response.setId(id);
            return this;
        }

        public PlaylistSongResponseBuilder playlistId(Long playlistId) {
            response.setPlaylistId(playlistId);
            return this;
        }

        public PlaylistSongResponseBuilder songId(Long songId) {
            response.setSongId(songId);
            return this;
        }

        public PlaylistSongResponseBuilder songTitle(String songTitle) {
            response.setSongTitle(songTitle);
            return this;
        }

        public PlaylistSongResponseBuilder artistName(String artistName) {
            response.setArtistName(artistName);
            return this;
        }

        public PlaylistSongResponseBuilder coverImage(String coverImage) {
            response.setCoverImage(coverImage);
            return this;
        }

        public PlaylistSongResponseBuilder orderIndex(Integer orderIndex) {
            response.setOrderIndex(orderIndex);
            return this;
        }

        public PlaylistSongResponseBuilder addedAt(LocalDateTime addedAt) {
            response.setAddedAt(addedAt);
            return this;
        }

        public PlaylistSongResponseBuilder duration(Integer duration) {
            response.setDuration(duration);
            return this;
        }

        public PlaylistSongResponse build() {
            return response;
        }
    }
}
