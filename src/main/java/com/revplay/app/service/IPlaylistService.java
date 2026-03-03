package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface IPlaylistService {
    PlaylistResponse create(PlaylistRequest request);

    PlaylistResponse getById(Long id);

    List<PlaylistResponse> getAll();

    List<PlaylistResponse> getByUserId(Long userId);

    List<PlaylistResponse> getPublicPlaylists();

    PlaylistResponse update(Long id, PlaylistRequest request);

    void softDelete(Long id);
}
