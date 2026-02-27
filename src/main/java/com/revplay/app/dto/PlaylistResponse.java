package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistResponse {
    private Long id;
    private Long userId;
    private String username;
    private String name;
    private String description;
    private String coverImage;
    private String privacy;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
