package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class AlbumRequest {
    @NotNull(message = "Artist ID is required")
    private Long artistId;

    @NotBlank(message = "Album name is required")
    @Size(max = 200)
    private String name;

    private String description;
    private LocalDate releaseDate;

    @Size(max = 500)
    private String coverImage;

    public AlbumRequest() {
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
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
}
