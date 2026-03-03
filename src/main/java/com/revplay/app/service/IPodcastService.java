package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface IPodcastService {
    PodcastResponse create(PodcastRequest request);

    PodcastResponse getById(Long id);

    List<PodcastResponse> getAll();

    List<PodcastResponse> getByArtistId(Long artistId);

    PodcastResponse update(Long id, PodcastRequest request);

    void delete(Long id);
}
