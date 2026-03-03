package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PodcastEpisodeMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IPodcastEpisodeService;
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
public class PodcastEpisodeServiceImpl implements IPodcastEpisodeService {
    private static final Logger log = LoggerFactory.getLogger(PodcastEpisodeServiceImpl.class);

    private final IPodcastEpisodeRepository episodeRepository;
    private final IPodcastRepository podcastRepository;
    private final PodcastEpisodeMapper mapper;

    @Override
    @Transactional
    public PodcastEpisodeResponse create(PodcastEpisodeRequest request) {
        log.info("Creating podcast episode: {} for podcast: {}", request.getTitle(), request.getPodcastId());
        Podcast podcast = podcastRepository.findById(request.getPodcastId())
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", request.getPodcastId()));
        PodcastEpisode episode = mapper.toEntity(request, podcast);
        return mapper.toResponse(episodeRepository.save(episode));
    }

    @Override
    public PodcastEpisodeResponse getById(Long id) {
        log.debug("Fetching podcast episode by id: {}", id);
        PodcastEpisode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PodcastEpisode", id));
        return mapper.toResponse(episode);
    }

    @Override
    public List<PodcastEpisodeResponse> getByPodcastId(Long podcastId) {
        log.debug("Fetching episodes for podcast id: {}", podcastId);
        return episodeRepository.findByPodcastIdOrderByReleaseDateDesc(podcastId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PodcastEpisodeResponse update(Long id, PodcastEpisodeRequest request) {
        log.info("Updating podcast episode id: {}", id);
        PodcastEpisode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PodcastEpisode", id));
        mapper.updateEntity(episode, request);
        return mapper.toResponse(episodeRepository.save(episode));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Deleting podcast episode id: {}", id);
        if (!episodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("PodcastEpisode", id);
        }
        episodeRepository.deleteById(id);
    }
}
