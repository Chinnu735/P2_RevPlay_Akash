package com.revplay.app.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PodcastMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.PodcastService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PodcastServiceImpl implements PodcastService {

    private static final Logger logger = LogManager.getLogger(PodcastServiceImpl.class);
    private final PodcastRepository podcastRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final PodcastMapper mapper;

    @Override
    public PodcastResponse create(PodcastRequest request) {
        ArtistProfile artist = artistProfileRepository.findById(request.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", request.getArtistId()));
        Podcast podcast = mapper.toEntity(request, artist);
        return mapper.toResponse(podcastRepository.save(podcast));
    }

    @Override
    public PodcastResponse getById(Long id) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", id));
        return mapper.toResponse(podcast);
    }

    @Override
    public List<PodcastResponse> getAll() {
        return podcastRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PodcastResponse> getByArtistId(Long artistId) {
        return podcastRepository.findByArtistId(artistId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PodcastResponse update(Long id, PodcastRequest request) {
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", id));
        mapper.updateEntity(podcast, request);
        return mapper.toResponse(podcastRepository.save(podcast));
    }

    @Override
    public void delete(Long id) {
        if (!podcastRepository.existsById(id)) {
            throw new ResourceNotFoundException("Podcast", id);
        }
        logger.info("Deleting Podcast with id: {}", id);
        podcastRepository.deleteById(id);
    }
}
