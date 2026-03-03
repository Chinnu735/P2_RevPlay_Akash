package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PlaylistMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IPlaylistService;
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
public class PlaylistServiceImpl implements IPlaylistService {
    private static final Logger log = LoggerFactory.getLogger(PlaylistServiceImpl.class);

    private final IPlaylistRepository playlistRepository;
    private final IUserRepository userRepository;
    private final PlaylistMapper mapper;

    @Override
    @Transactional
    public PlaylistResponse create(PlaylistRequest request) {
        log.info("Creating playlist: {} for userId: {}", request.getName(), request.getUserId());
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));
        Playlist playlist = mapper.toEntity(request, user);
        return mapper.toResponse(playlistRepository.save(playlist));
    }

    @Override
    public PlaylistResponse getById(Long id) {
        log.debug("Fetching playlist by id: {}", id);
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", id));
        return mapper.toResponse(playlist);
    }

    @Override
    public List<PlaylistResponse> getAll() {
        log.debug("Fetching all playlists");
        return playlistRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistResponse> getByUserId(Long userId) {
        log.debug("Fetching playlists for userId: {}", userId);
        return playlistRepository.findByUserIdAndIsDeletedNot(userId, 1).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistResponse> getPublicPlaylists() {
        log.debug("Fetching public playlists");
        return playlistRepository.findByPrivacyAndIsDeleted("public", 0).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlaylistResponse update(Long id, PlaylistRequest request) {
        log.info("Updating playlist id: {}", id);
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", id));
        mapper.updateEntity(playlist, request);
        return mapper.toResponse(playlistRepository.save(playlist));
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        log.info("Soft-deleting playlist id: {}", id);
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", id));
        playlist.setIsDeleted(1);
        playlistRepository.save(playlist);
    }
}
