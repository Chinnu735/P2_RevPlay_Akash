package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumResponse {
    private Long id;
    private Long artistId;
    private String artistName;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private String coverImage;
    private LocalDateTime createdAt;
    private Integer isDeleted;
}
