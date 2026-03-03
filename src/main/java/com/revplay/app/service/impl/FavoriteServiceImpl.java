package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.FavoriteMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IFavoriteService;
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
public class FavoriteServiceImpl implements IFavoriteService {
    private static final Logger log = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    private final IFavoriteRepository favoriteRepository;
    private final IUserRepository userRepository;
    private final ISongRepository songRepository;
    private final FavoriteMapper mapper;

    @Override
    @Transactional
    public FavoriteResponse addFavorite(FavoriteRequest request) {
        log.info("Adding favorite - userId: {}, songId: {}", request.getUserId(), request.getSongId());
        if (favoriteRepository.existsByUserIdAndSongId(request.getUserId(), request.getSongId())) {
            throw new DuplicateResourceException("Song already in favorites");
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));
        Song song = songRepository.findById(request.getSongId())
                .orElseThrow(() -> new ResourceNotFoundException("Song", request.getSongId()));
        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setSong(song);
        favorite.setAddedAt(LocalDateTime.now());
        return mapper.toResponse(favoriteRepository.save(favorite));
    }

    @Override
    @Transactional
    public void removeFavorite(Long userId, Long songId) {
        log.info("Removing favorite - userId: {}, songId: {}", userId, songId);
        if (!favoriteRepository.existsByUserIdAndSongId(userId, songId)) {
            throw new ResourceNotFoundException("Favorite not found for user " + userId + " and song " + songId);
        }
        favoriteRepository.deleteByUserIdAndSongId(userId, songId);
    }

    @Override
    public List<FavoriteResponse> getFavoritesByUserId(Long userId) {
        log.debug("Fetching favorites for userId: {}", userId);
        return favoriteRepository.findByUserId(userId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(Long userId, Long songId) {
        log.debug("Checking if song {} is favorited by user {}", songId, userId);
        return favoriteRepository.existsByUserIdAndSongId(userId, songId);
    }
}
