package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public GenreResponse toResponse(Genre genre) {
        return GenreResponse.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public Genre toEntity(GenreRequest request) {
        Genre genre = new Genre();
        genre.setName(request.getName());
        return genre;
    }
}
