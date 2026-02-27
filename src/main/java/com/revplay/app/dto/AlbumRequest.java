package com.revplay.app.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRequest {
    @NotNull(message = "Artist ID is required")
    private Long artistId;

    @NotBlank(message = "Album name is required")
    @Size(max = 200)
    private String name;

    private String description;
    private LocalDate releaseDate;

    @Size(max = 500)
    private String coverImage;
}
