package com.revplay.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreRequest {
    @NotBlank(message = "Genre name is required")
    @Size(max = 100, message = "Genre name must be at most 100 characters")
    private String name;
}
