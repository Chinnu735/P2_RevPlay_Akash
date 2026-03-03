package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PodcastMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IPodcastService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PodcastServiceImpl implements IPodcastService {
    private static final Logger log = LoggerFactory.getLogger(PodcastServiceImpl.class);

    private final IPodcastRepository podcastRepository;
    private final IArtistProfileRepository artistProfileRepository;
    private final PodcastMapper mapper;

    @Override
    @Transactional
    public PodcastResponse create(PodcastRequest request) {
        log.info("Creating podcast: {} for artist: {}", request.getTitle(), request.getArtistId());
        ArtistProfile artist = artistProfileRepository.findById(request.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", request.getArtistId()));
        Podcast podcast = mapper.toEntity(request, artist);
        return mapper.toResponse(podcastRepository.save(podcast));
    }

    @Override
    public PodcastResponse getById(Long id) {
        log.debug("Fetching podcast by id: {}", id);
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", id));
        return mapper.toResponse(podcast);
    }

    @Override
    public List<PodcastResponse> getAll() {
        log.debug("Fetching all podcasts");
        return podcastRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PodcastResponse> getByArtistId(Long artistId) {
        log.debug("Fetching podcasts for artist id: {}", artistId);
        return podcastRepository.findByArtistId(artistId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PodcastResponse update(Long id, PodcastRequest request) {
        log.info("Updating podcast id: {}", id);
        Podcast podcast = podcastRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", id));
        mapper.updateEntity(podcast, request);
        return mapper.toResponse(podcastRepository.save(podcast));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting podcast id: {}", id);
        if (!podcastRepository.existsById(id)) {
            throw new ResourceNotFoundException("Podcast", id);
        }
        podcastRepository.deleteById(id);
    }
}
