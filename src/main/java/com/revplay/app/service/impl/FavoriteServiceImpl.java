package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.FavoriteMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final FavoriteMapper mapper;

    @Override
    public FavoriteResponse addFavorite(FavoriteRequest request) {
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
        if (!favoriteRepository.existsByUserIdAndSongId(userId, songId)) {
            throw new ResourceNotFoundException("Favorite not found for user " + userId + " and song " + songId);
        }
        favoriteRepository.deleteByUserIdAndSongId(userId, songId);
    }

    @Override
    public List<FavoriteResponse> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isFavorite(Long userId, Long songId) {
        return favoriteRepository.existsByUserIdAndSongId(userId, songId);
    }
}
