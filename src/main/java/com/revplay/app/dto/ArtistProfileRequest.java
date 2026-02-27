package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistProfileRequest {
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
    private String twitterLink;

    @Size(max = 255)
    private String youtubeLink;

    @Size(max = 255)
    private String spotifyLink;

    @Size(max = 255)
    private String websiteLink;
}
