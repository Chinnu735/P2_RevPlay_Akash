package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {
    public SongResponse toResponse(Song song) {
        return SongResponse.builder()
                .id(song.getId())
                .artistId(song.getArtist().getId())
                .artistName(song.getArtist().getArtistName())
                .albumId(song.getAlbum() != null ? song.getAlbum().getId() : null)
                .albumName(song.getAlbum() != null ? song.getAlbum().getName() : null)
                .genreId(song.getGenre() != null ? song.getGenre().getId() : null)
                .genreName(song.getGenre() != null ? song.getGenre().getName() : null)
                .title(song.getTitle())
                .audioUrl(song.getAudioUrl())
                .fileSize(song.getFileSize())
                .releaseDate(song.getReleaseDate())
                .coverImage(song.getCoverImage())
                .visibility(song.getVisibility())
                .status(song.getStatus())
                .createdAt(song.getCreatedAt())
                .isDeleted(song.getIsDeleted())
                .build();
    }

    public Song toEntity(SongRequest request, ArtistProfile artist, Album album, Genre genre) {
        Song song = new Song();
        song.setArtist(artist);
        song.setAlbum(album);
        song.setGenre(genre);
        song.setTitle(request.getTitle());
        song.setAudioUrl(request.getAudioUrl());
        song.setFileSize(request.getFileSize());
        song.setReleaseDate(request.getReleaseDate());
        song.setCoverImage(request.getCoverImage());
        song.setVisibility(request.getVisibility() != null ? request.getVisibility() : "public");
        song.setStatus(request.getStatus() != null ? request.getStatus() : "active");
        song.setIsDeleted(0);
        return song;
    }

    public void updateEntity(Song song, SongRequest request, Album album, Genre genre) {
        song.setAlbum(album);
        song.setGenre(genre);
        song.setTitle(request.getTitle());
        song.setAudioUrl(request.getAudioUrl());
        song.setFileSize(request.getFileSize());
        song.setReleaseDate(request.getReleaseDate());
        if (request.getCoverImage() != null) {
            song.setCoverImage(request.getCoverImage());
        }
        song.setVisibility(request.getVisibility());
        song.setStatus(request.getStatus());
    }
}
