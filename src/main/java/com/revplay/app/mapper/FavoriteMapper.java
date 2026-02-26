package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {
    public FavoriteResponse toResponse(Favorite favorite) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .userId(favorite.getUser().getId())
                .songId(favorite.getSong().getId())
                .songTitle(favorite.getSong().getTitle())
                .artistName(favorite.getSong().getArtist().getArtistName())
                .addedAt(favorite.getAddedAt())
                .build();
    }
}
