package com.revplay.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Song ID is required")
    private Long songId;
}
