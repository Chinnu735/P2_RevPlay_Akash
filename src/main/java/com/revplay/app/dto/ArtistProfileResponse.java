package com.revplay.app.dto;

import java.time.LocalDateTime;

public class ArtistProfileResponse {
    private Long id;
    private Long userId;
    private String artistName;
    private Long genreId;
    private String genreName;
    private String bannerImage;
    private String instagramLink;
    private String youtubeLink;
    private LocalDateTime createdAt;

    public ArtistProfileResponse() {
    }

    public static ArtistProfileResponseBuilder builder() {
        return new ArtistProfileResponseBuilder();
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

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
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

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static class ArtistProfileResponseBuilder {
        private ArtistProfileResponse response = new ArtistProfileResponse();

        public ArtistProfileResponseBuilder id(Long id) {
            response.setId(id);
            return this;
        }

        public ArtistProfileResponseBuilder userId(Long userId) {
            response.setUserId(userId);
            return this;
        }

        public ArtistProfileResponseBuilder artistName(String artistName) {
            response.setArtistName(artistName);
            return this;
        }

        public ArtistProfileResponseBuilder genreId(Long genreId) {
            response.setGenreId(genreId);
            return this;
        }

        public ArtistProfileResponseBuilder genreName(String genreName) {
            response.setGenreName(genreName);
            return this;
        }

        public ArtistProfileResponseBuilder bannerImage(String bannerImage) {
            response.setBannerImage(bannerImage);
            return this;
        }

        public ArtistProfileResponseBuilder instagramLink(String instagramLink) {
            response.setInstagramLink(instagramLink);
            return this;
        }

        public ArtistProfileResponseBuilder youtubeLink(String youtubeLink) {
            response.setYoutubeLink(youtubeLink);
            return this;
        }

        public ArtistProfileResponseBuilder createdAt(LocalDateTime createdAt) {
            response.setCreatedAt(createdAt);
            return this;
        }

        public ArtistProfileResponse build() {
            return response;
        }
    }
}
