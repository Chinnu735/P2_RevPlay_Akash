package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface IAlbumService {
    AlbumResponse create(AlbumRequest request);

    AlbumResponse getById(Long id);

    List<AlbumResponse> getAll();

    List<AlbumResponse> getByArtistId(Long artistId);

    AlbumResponse update(Long id, AlbumRequest request);

    void softDelete(Long id);
}
