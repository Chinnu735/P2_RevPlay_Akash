package com.revplay.app.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumDetailResponse {
    private Long albumId;
    private String albumName;
    private String description;
    private String coverImageUrl;
    private String releaseDate;
    private String artistName;
    private Long artistId;
    private List<SongResponse> tracks;
}
