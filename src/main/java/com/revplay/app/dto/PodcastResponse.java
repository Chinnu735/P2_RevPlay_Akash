package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PodcastResponse {
    private Long id;
    private Long artistId;
    private String artistName;
    private String title;
    private String description;
    private String coverImage;
    private LocalDateTime createdAt;
}
