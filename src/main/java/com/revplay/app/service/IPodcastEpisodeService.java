package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface IPodcastEpisodeService {
    PodcastEpisodeResponse create(PodcastEpisodeRequest request);

    PodcastEpisodeResponse getById(Long id);

    List<PodcastEpisodeResponse> getByPodcastId(Long podcastId);

    PodcastEpisodeResponse update(Long id, PodcastEpisodeRequest request);

    void delete(Long id);
}
