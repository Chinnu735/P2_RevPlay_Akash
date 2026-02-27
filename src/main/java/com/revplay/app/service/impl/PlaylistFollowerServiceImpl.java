package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PlaylistFollowerMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.PlaylistFollowerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistFollowerServiceImpl implements PlaylistFollowerService {

    private final PlaylistFollowerRepository followerRepository;
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final PlaylistFollowerMapper mapper;

    @Override
    public PlaylistFollowerResponse followPlaylist(PlaylistFollowerRequest request) {
        if (followerRepository.existsByPlaylistIdAndUserId(request.getPlaylistId(), request.getUserId())) {
            throw new DuplicateResourceException("Already following this playlist");
        }
        Playlist playlist = playlistRepository.findById(request.getPlaylistId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", request.getPlaylistId()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));

        PlaylistFollower follower = new PlaylistFollower();
        follower.setPlaylist(playlist);
        follower.setUser(user);
        follower.setFollowedAt(LocalDateTime.now());
        return mapper.toResponse(followerRepository.save(follower));
    }

    @Override
    @Transactional
    public void unfollowPlaylist(Long playlistId, Long userId) {
        if (!followerRepository.existsByPlaylistIdAndUserId(playlistId, userId)) {
            throw new ResourceNotFoundException("Not following this playlist");
        }
        followerRepository.deleteByPlaylistIdAndUserId(playlistId, userId);
    }

    @Override
    public List<PlaylistFollowerResponse> getFollowersByPlaylistId(Long playlistId) {
        return followerRepository.findByPlaylistId(playlistId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PlaylistFollowerResponse> getFollowedPlaylistsByUserId(Long userId) {
        return followerRepository.findByUserId(userId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
