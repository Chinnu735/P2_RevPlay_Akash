package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistProfileResponse {
    private Long id;
    private Long userId;
    private String artistName;
    private Long genreId;
    private String genreName;
    private String bannerImage;
    private String instagramLink;
    private String twitterLink;
    private String youtubeLink;
    private String spotifyLink;
    private String websiteLink;
    private LocalDateTime createdAt;
}
