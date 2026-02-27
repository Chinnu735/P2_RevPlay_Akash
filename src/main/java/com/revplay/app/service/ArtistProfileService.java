package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface ArtistProfileService {
    ArtistProfileResponse create(ArtistProfileRequest request);

    ArtistProfileResponse getById(Long id);

    ArtistProfileResponse getByUserId(Long userId);

    List<ArtistProfileResponse> getAll();

    List<ArtistProfileResponse> getByGenreId(Long genreId);

    ArtistProfileResponse update(Long id, ArtistProfileRequest request);

    void delete(Long id);
}
