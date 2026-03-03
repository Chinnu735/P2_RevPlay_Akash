package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class PlaylistSongMapper {
    public PlaylistSongResponse toResponse(PlaylistSong playlistSong) {
        return PlaylistSongResponse.builder()
                .playlistId(playlistSong.getPlaylist().getId())
                .songId(playlistSong.getSong().getId())
                .songTitle(playlistSong.getSong().getTitle())
                .artistName(playlistSong.getSong().getArtist().getArtistName())
                .duration(playlistSong.getSong().getDuration())
                .orderIndex(playlistSong.getOrderIndex())
                .build();
    }
}
