package com.revplay.app.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoriteResponse {
    private Long id;
    private Long userId;
    private Long songId;
    private String songTitle;
    private String artistName;
    private LocalDateTime addedAt;
}
