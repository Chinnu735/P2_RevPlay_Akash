package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface IPlaylistSongService {
    PlaylistSongResponse addSongToPlaylist(PlaylistSongRequest request);

    void removeSongFromPlaylist(Long playlistId, Long songId);

    List<PlaylistSongResponse> getSongsByPlaylistId(Long playlistId);

    void reorderSong(Long playlistId, Long songId, Integer newOrderIndex);
}
