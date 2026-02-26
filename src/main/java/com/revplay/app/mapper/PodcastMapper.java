package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class PodcastMapper {
    public PodcastResponse toResponse(Podcast podcast) {
        return PodcastResponse.builder()
                .id(podcast.getId())
                .artistId(podcast.getArtist().getId())
                .artistName(podcast.getArtist().getArtistName())
                .title(podcast.getTitle())
                .description(podcast.getDescription())
                .coverImage(podcast.getCoverImage())
                .createdAt(podcast.getCreatedAt())
                .build();
    }

    public Podcast toEntity(PodcastRequest request, ArtistProfile artist) {
        Podcast podcast = new Podcast();
        podcast.setArtist(artist);
        podcast.setTitle(request.getTitle());
        podcast.setDescription(request.getDescription());
        podcast.setCoverImage(request.getCoverImage());
        return podcast;
    }

    public void updateEntity(Podcast podcast, PodcastRequest request) {
        podcast.setTitle(request.getTitle());
        podcast.setDescription(request.getDescription());
        podcast.setCoverImage(request.getCoverImage());
    }
}
