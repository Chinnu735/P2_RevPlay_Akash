package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PlaylistFollowerMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IPlaylistFollowerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaylistFollowerServiceImpl implements IPlaylistFollowerService {
    private static final Logger log = LoggerFactory.getLogger(PlaylistFollowerServiceImpl.class);

    private final IPlaylistFollowerRepository followerRepository;
    private final IPlaylistRepository playlistRepository;
    private final IUserRepository userRepository;
    private final PlaylistFollowerMapper mapper;

    @Override
    @Transactional
    public PlaylistFollowerResponse followPlaylist(PlaylistFollowerRequest request) {
        log.info("User {} is following playlist {}", request.getUserId(), request.getPlaylistId());
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
        log.info("User {} unfollowed playlist {}", userId, playlistId);
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
