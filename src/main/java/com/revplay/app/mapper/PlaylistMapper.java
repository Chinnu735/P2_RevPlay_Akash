package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class PlaylistMapper {
    public PlaylistResponse toResponse(Playlist playlist) {
        return PlaylistResponse.builder()
                .id(playlist.getId())
                .userId(playlist.getUser().getId())
                .username(playlist.getUser().getUsername())
                .name(playlist.getName())
                .description(playlist.getDescription())
                .coverImage(playlist.getCoverImage())
                .privacy(playlist.getPrivacy())
                .isDeleted(playlist.getIsDeleted())
                .createdAt(playlist.getCreatedAt())
                .updatedAt(playlist.getUpdatedAt())
                .build();
    }

    public Playlist toEntity(PlaylistRequest request, User user) {
        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setName(request.getName());
        playlist.setDescription(request.getDescription());
        playlist.setCoverImage(request.getCoverImage());
        playlist.setPrivacy(request.getPrivacy() != null ? request.getPrivacy() : "public");
        playlist.setIsDeleted(0);
        return playlist;
    }

    public void updateEntity(Playlist playlist, PlaylistRequest request) {
        playlist.setName(request.getName());
        playlist.setDescription(request.getDescription());
        playlist.setCoverImage(request.getCoverImage());
        playlist.setPrivacy(request.getPrivacy());
    }
}
