package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface ISongService {
    SongResponse create(SongRequest request);

    SongResponse getById(Long id);

    List<SongResponse> getAll();

    List<SongResponse> getByArtistId(Long artistId);

    List<SongResponse> getByAlbumId(Long albumId);

    List<SongResponse> getByGenreId(Long genreId);

    List<SongResponse> search(String title);

    List<SongResponse> getTrendingSongs();

    SongResponse update(Long id, SongRequest request);

    void updateDuration(Long id, Integer duration);

    void softDelete(Long id);

    List<SongResponse> getRecentSongs();

    List<SongResponse> getSongsByGenre(Long genreId);
}
