package com.revplay.app.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.ArtistProfileMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.ArtistProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistProfileServiceImpl implements ArtistProfileService {

    private static final Logger logger = LogManager.getLogger(ArtistProfileServiceImpl.class);
    private final ArtistProfileRepository artistProfileRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final ArtistProfileMapper mapper;

    @Override
    public ArtistProfileResponse create(ArtistProfileRequest request) {
        if (artistProfileRepository.existsByUserId(request.getUserId())) {
            throw new DuplicateResourceException("Artist profile already exists for user: " + request.getUserId());
        }
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));
        Genre genre = null;
        if (request.getGenreId() != null) {
            genre = genreRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre", request.getGenreId()));
        }
        ArtistProfile profile = mapper.toEntity(request, user, genre);
        return mapper.toResponse(artistProfileRepository.save(profile));
    }

    @Override
    public ArtistProfileResponse getById(Long id) {
        ArtistProfile profile = artistProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", id));
        return mapper.toResponse(profile);
    }

    @Override
    public ArtistProfileResponse getByUserId(Long userId) {
        ArtistProfile profile = artistProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile not found for user: " + userId));
        return mapper.toResponse(profile);
    }

    @Override
    public List<ArtistProfileResponse> getAll() {
        return artistProfileRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtistProfileResponse> getByGenreId(Long genreId) {
        return artistProfileRepository.findByGenreId(genreId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ArtistProfileResponse update(Long id, ArtistProfileRequest request) {
        ArtistProfile profile = artistProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", id));
        Genre genre = null;
        if (request.getGenreId() != null) {
            genre = genreRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre", request.getGenreId()));
        }
        mapper.updateEntity(profile, request, genre);
        return mapper.toResponse(artistProfileRepository.save(profile));
    }

    @Override
    public void delete(Long id) {
        if (!artistProfileRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtistProfile", id);
        }
        logger.info("Deleting ArtistProfile with id: {}", id);
        artistProfileRepository.deleteById(id);
    }
}
