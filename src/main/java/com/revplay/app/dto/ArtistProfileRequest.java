package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;

public class ArtistProfileRequest implements Serializable {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Artist name is required")
    @Size(max = 200)
    private String artistName;

    private Long genreId;

    @Size(max = 500)
    private String bannerImage;

    @Size(max = 255)
    private String instagramLink;

    @Size(max = 255)
    private String youtubeLink;

    public ArtistProfileRequest() {
    }

    public ArtistProfileRequest(Long userId, String artistName, Long genreId, String bannerImage, String instagramLink,
            String youtubeLink) {
        this.userId = userId;
        this.artistName = artistName;
        this.genreId = genreId;
        this.bannerImage = bannerImage;
        this.instagramLink = instagramLink;
        this.youtubeLink = youtubeLink;
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
}
