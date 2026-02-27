package com.revplay.app.rest;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistRegisterRequest {
    private String email;
    private String password;
    private String username;
    private String artistName;
    private Long genreId;
    private String bannerImage;
    private String instagramLink;
    private String twitterLink;
    private String youtubeLink;
    private String spotifyLink;
    private String websiteLink;
    private String securityQuestion;
    private String securityAnswer;
}
