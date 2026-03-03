package com.revplay.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SongResponse {
    private Long id;
    private Long artistId;
    private String artistName;
    private Long albumId;
    private String albumName;
    private Long genreId;
    private String genreName;
    private String title;
    private String audioUrl;
    private Long fileSize;
    private Integer duration;
    private LocalDate releaseDate;
    private String coverImage;
    private String visibility;
    private String status;
    private LocalDateTime createdAt;
    private Integer isDeleted;

    public SongResponse() {
    }

    public static SongResponseBuilder builder() {
        return new SongResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public static class SongResponseBuilder {
        private SongResponse response = new SongResponse();

        public SongResponseBuilder id(Long id) {
            response.setId(id);
            return this;
        }

        public SongResponseBuilder artistId(Long artistId) {
            response.setArtistId(artistId);
            return this;
        }

        public SongResponseBuilder artistName(String artistName) {
            response.setArtistName(artistName);
            return this;
        }

        public SongResponseBuilder albumId(Long albumId) {
            response.setAlbumId(albumId);
            return this;
        }

        public SongResponseBuilder albumName(String albumName) {
            response.setAlbumName(albumName);
            return this;
        }

        public SongResponseBuilder genreId(Long genreId) {
            response.setGenreId(genreId);
            return this;
        }

        public SongResponseBuilder genreName(String genreName) {
            response.setGenreName(genreName);
            return this;
        }

        public SongResponseBuilder title(String title) {
            response.setTitle(title);
            return this;
        }

        public SongResponseBuilder audioUrl(String audioUrl) {
            response.setAudioUrl(audioUrl);
            return this;
        }

        public SongResponseBuilder fileSize(Long fileSize) {
            response.setFileSize(fileSize);
            return this;
        }

        public SongResponseBuilder duration(Integer duration) {
            response.setDuration(duration);
            return this;
        }

        public SongResponseBuilder releaseDate(LocalDate releaseDate) {
            response.setReleaseDate(releaseDate);
            return this;
        }

        public SongResponseBuilder coverImage(String coverImage) {
            response.setCoverImage(coverImage);
            return this;
        }

        public SongResponseBuilder visibility(String visibility) {
            response.setVisibility(visibility);
            return this;
        }

        public SongResponseBuilder status(String status) {
            response.setStatus(status);
            return this;
        }

        public SongResponseBuilder createdAt(LocalDateTime createdAt) {
            response.setCreatedAt(createdAt);
            return this;
        }

        public SongResponseBuilder isDeleted(Integer isDeleted) {
            response.setIsDeleted(isDeleted);
            return this;
        }

        public SongResponse build() {
            return response;
        }
    }
}
