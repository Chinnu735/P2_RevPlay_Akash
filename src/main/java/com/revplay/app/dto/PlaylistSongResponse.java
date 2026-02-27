package com.revplay.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistSongResponse {
    private Long playlistId;
    private Long songId;
    private String songTitle;
    private String artistName;
    private Integer orderIndex;
}
