package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PlaylistMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final PlaylistMapper mapper;

    @Override
    public PlaylistResponse create(PlaylistRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));
        Playlist playlist = mapper.toEntity(request, user);
        return mapper.toResponse(playlistRepository.save(playlist));
    }

    @Override
    public PlaylistResponse getById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", id));
        return mapper.toResponse(playlist);
    }

    @Override
    public List<PlaylistResponse> getAll() {
        return playlistRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistResponse> getByUserId(Long userId) {
        return playlistRepository.findByUserIdAndIsDeletedNot(userId, 1).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistResponse> getPublicPlaylists() {
        return playlistRepository.findByPrivacyAndIsDeleted("public", 0).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PlaylistResponse update(Long id, PlaylistRequest request) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", id));
        mapper.updateEntity(playlist, request);
        return mapper.toResponse(playlistRepository.save(playlist));
    }

    @Override
    public void softDelete(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", id));
        playlist.setIsDeleted(1);
        playlistRepository.save(playlist);
    }
}
