package com.revplay.app.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AlbumResponse {
    private Long id;
    private Long artistId;
    private String artistName;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private String coverImage;
    private LocalDateTime createdAt;
    private Integer isDeleted;

    public AlbumResponse() {
    }

    public static AlbumResponseBuilder builder() {
        return new AlbumResponseBuilder();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static class AlbumResponseBuilder {
        private AlbumResponse response = new AlbumResponse();

        public AlbumResponseBuilder id(Long id) {
            response.setId(id);
            return this;
        }

        public AlbumResponseBuilder artistId(Long artistId) {
            response.setArtistId(artistId);
            return this;
        }

        public AlbumResponseBuilder artistName(String artistName) {
            response.setArtistName(artistName);
            return this;
        }

        public AlbumResponseBuilder name(String name) {
            response.setName(name);
            return this;
        }

        public AlbumResponseBuilder description(String description) {
            response.setDescription(description);
            return this;
        }

        public AlbumResponseBuilder releaseDate(LocalDate releaseDate) {
            response.setReleaseDate(releaseDate);
            return this;
        }

        public AlbumResponseBuilder coverImage(String coverImage) {
            response.setCoverImage(coverImage);
            return this;
        }

        public AlbumResponseBuilder createdAt(LocalDateTime createdAt) {
            response.setCreatedAt(createdAt);
            return this;
        }

        public AlbumResponseBuilder isDeleted(Integer isDeleted) {
            response.setIsDeleted(isDeleted);
            return this;
        }

        public AlbumResponse build() {
            return response;
        }
    }
}
