package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {
    public AlbumResponse toResponse(Album album) {
        return AlbumResponse.builder()
                .id(album.getId())
                .artistId(album.getArtist().getId())
                .artistName(album.getArtist().getArtistName())
                .name(album.getName())
                .description(album.getDescription())
                .releaseDate(album.getReleaseDate())
                .coverImage(album.getCoverImage())
                .createdAt(album.getCreatedAt())
                .isDeleted(album.getIsDeleted())
                .build();
    }

    public Album toEntity(AlbumRequest request, ArtistProfile artist) {
        Album album = new Album();
        album.setArtist(artist);
        album.setName(request.getName());
        album.setDescription(request.getDescription());
        album.setReleaseDate(request.getReleaseDate());
        album.setCoverImage(request.getCoverImage());
        album.setIsDeleted(0);
        return album;
    }

    public void updateEntity(Album album, AlbumRequest request) {
        album.setName(request.getName());
        album.setDescription(request.getDescription());
        album.setReleaseDate(request.getReleaseDate());
        album.setCoverImage(request.getCoverImage());
    }
}
