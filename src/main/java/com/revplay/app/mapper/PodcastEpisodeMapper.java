package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class PodcastEpisodeMapper {
    public PodcastEpisodeResponse toResponse(PodcastEpisode episode) {
        return PodcastEpisodeResponse.builder()
                .id(episode.getId())
                .podcastId(episode.getPodcast().getId())
                .podcastTitle(episode.getPodcast().getTitle())
                .title(episode.getTitle())
                .description(episode.getDescription())
                .audioUrl(episode.getAudioUrl())
                .duration(episode.getDuration())
                .releaseDate(episode.getReleaseDate())
                .createdAt(episode.getCreatedAt())
                .build();
    }

    public PodcastEpisode toEntity(PodcastEpisodeRequest request, Podcast podcast) {
        PodcastEpisode episode = new PodcastEpisode();
        episode.setPodcast(podcast);
        episode.setTitle(request.getTitle());
        episode.setDescription(request.getDescription());
        episode.setAudioUrl(request.getAudioUrl());
        episode.setDuration(request.getDuration());
        episode.setReleaseDate(request.getReleaseDate());
        return episode;
    }

    public void updateEntity(PodcastEpisode episode, PodcastEpisodeRequest request) {
        episode.setTitle(request.getTitle());
        episode.setDescription(request.getDescription());
        episode.setAudioUrl(request.getAudioUrl());
        episode.setDuration(request.getDuration());
        episode.setReleaseDate(request.getReleaseDate());
    }
}
