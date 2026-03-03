package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface IPlaylistFollowerService {
    PlaylistFollowerResponse followPlaylist(PlaylistFollowerRequest request);

    void unfollowPlaylist(Long playlistId, Long userId);

    List<PlaylistFollowerResponse> getFollowersByPlaylistId(Long playlistId);

    List<PlaylistFollowerResponse> getFollowedPlaylistsByUserId(Long userId);
}
