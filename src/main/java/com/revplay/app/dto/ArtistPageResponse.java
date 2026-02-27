package com.revplay.app.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArtistPageResponse {
    private Long artistProfileId;
    private String artistName;
    private String genreName;
    private String bannerImage;
    private String instagramLink;
    private String twitterLink;
    private String youtubeLink;
    private String spotifyLink;
    private String websiteLink;
    private List<SongResponse> songs;
    private List<AlbumResponse> albums;
}
