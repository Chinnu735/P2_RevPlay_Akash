package com.revplay.app.dto;

import java.time.LocalDateTime;

public class PodcastResponse {
    private Long id;
    private Long artistId;
    private String artistName;
    private String title;
    private String description;
    private String coverImage;
    private LocalDateTime createdAt;

    public PodcastResponse() {
    }

    public static PodcastResponseBuilder builder() {
        return new PodcastResponseBuilder();
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static class PodcastResponseBuilder {
        private PodcastResponse response = new PodcastResponse();

        public PodcastResponseBuilder id(Long id) {
            response.setId(id);
            return this;
        }

        public PodcastResponseBuilder artistId(Long artistId) {
            response.setArtistId(artistId);
            return this;
        }

        public PodcastResponseBuilder artistName(String artistName) {
            response.setArtistName(artistName);
            return this;
        }

        public PodcastResponseBuilder title(String title) {
            response.setTitle(title);
            return this;
        }

        public PodcastResponseBuilder description(String description) {
            response.setDescription(description);
            return this;
        }

        public PodcastResponseBuilder coverImage(String coverImage) {
            response.setCoverImage(coverImage);
            return this;
        }

        public PodcastResponseBuilder createdAt(LocalDateTime createdAt) {
            response.setCreatedAt(createdAt);
            return this;
        }

        public PodcastResponse build() {
            return response;
        }
    }
}
