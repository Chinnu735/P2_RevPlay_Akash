package com.revplay.app.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PodcastEpisodeMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.PodcastEpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PodcastEpisodeServiceImpl implements PodcastEpisodeService {

    private static final Logger logger = LogManager.getLogger(PodcastEpisodeServiceImpl.class);
    private final PodcastEpisodeRepository episodeRepository;
    private final PodcastRepository podcastRepository;
    private final PodcastEpisodeMapper mapper;

    @Override
    public PodcastEpisodeResponse create(PodcastEpisodeRequest request) {
        Podcast podcast = podcastRepository.findById(request.getPodcastId())
                .orElseThrow(() -> new ResourceNotFoundException("Podcast", request.getPodcastId()));
        PodcastEpisode episode = mapper.toEntity(request, podcast);
        return mapper.toResponse(episodeRepository.save(episode));
    }

    @Override
    public PodcastEpisodeResponse getById(Long id) {
        PodcastEpisode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PodcastEpisode", id));
        return mapper.toResponse(episode);
    }

    @Override
    public List<PodcastEpisodeResponse> getByPodcastId(Long podcastId) {
        return episodeRepository.findByPodcastIdOrderByReleaseDateDesc(podcastId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PodcastEpisodeResponse update(Long id, PodcastEpisodeRequest request) {
        PodcastEpisode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PodcastEpisode", id));
        mapper.updateEntity(episode, request);
        return mapper.toResponse(episodeRepository.save(episode));
    }

    @Override
    public void delete(Long id) {
        if (!episodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("PodcastEpisode", id);
        }
        logger.info("Deleting PodcastEpisode with id: {}", id);
        episodeRepository.deleteById(id);
    }
}
