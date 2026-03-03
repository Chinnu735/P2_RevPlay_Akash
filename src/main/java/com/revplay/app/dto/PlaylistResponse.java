package com.revplay.app.dto;

import java.time.LocalDateTime;

public class PlaylistResponse {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String description;
    private String coverImage;
    private String privacy;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PlaylistResponse() {
    }

    public static PlaylistResponseBuilder builder() {
        return new PlaylistResponseBuilder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class PlaylistResponseBuilder {
        private PlaylistResponse response = new PlaylistResponse();

        public PlaylistResponseBuilder id(Long id) {
            response.setId(id);
            return this;
        }

        public PlaylistResponseBuilder userId(Long userId) {
            response.setUserId(userId);
            return this;
        }

        public PlaylistResponseBuilder username(String username) {
            response.setUsername(username);
            return this;
        }

        public PlaylistResponseBuilder name(String name) {
            response.setName(name);
            return this;
        }

        public PlaylistResponseBuilder description(String description) {
            response.setDescription(description);
            return this;
        }

        public PlaylistResponseBuilder coverImage(String coverImage) {
            response.setCoverImage(coverImage);
            return this;
        }

        public PlaylistResponseBuilder privacy(String privacy) {
            response.setPrivacy(privacy);
            return this;
        }

        public PlaylistResponseBuilder isDeleted(Integer isDeleted) {
            response.setIsDeleted(isDeleted);
            return this;
        }

        public PlaylistResponseBuilder createdAt(LocalDateTime createdAt) {
            response.setCreatedAt(createdAt);
            return this;
        }

        public PlaylistResponseBuilder updatedAt(LocalDateTime updatedAt) {
            response.setUpdatedAt(updatedAt);
            return this;
        }

        public PlaylistResponse build() {
            return response;
        }
    }
}
