package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongResponse {
    private Long id;
    private Long artistId;
    private String artistName;
    private Long albumId;
    private String albumName;
    private Long genreId;
    private String genreName;
    private String title;
    private String audioUrl;
    private Long fileSize;
    private LocalDate releaseDate;
    private String coverImage;
    private String visibility;
    private String status;
    private LocalDateTime createdAt;
    private Integer isDeleted;
}
