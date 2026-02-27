package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface GenreService {
    GenreResponse create(GenreRequest request);

    GenreResponse getById(Long id);

    List<GenreResponse> getAll();

    GenreResponse update(Long id, GenreRequest request);

    void delete(Long id);
}
