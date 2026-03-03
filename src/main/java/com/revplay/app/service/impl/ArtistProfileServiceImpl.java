package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.ArtistProfileMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IArtistProfileService;
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
public class ArtistProfileServiceImpl implements IArtistProfileService {
    private static final Logger log = LoggerFactory.getLogger(ArtistProfileServiceImpl.class);

    private final IArtistProfileRepository artistProfileRepository;
    private final IUserRepository userRepository;
    private final IGenreRepository genreRepository;
    private final ArtistProfileMapper mapper;

    @Override
    @Transactional
    public ArtistProfileResponse create(ArtistProfileRequest request) {
        log.info("Creating artist profile for userId: {}", request.getUserId());
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
        log.debug("Fetching artist profile by id: {}", id);
        ArtistProfile profile = artistProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", id));
        return mapper.toResponse(profile);
    }

    @Override
    public ArtistProfileResponse getByUserId(Long userId) {
        log.debug("Fetching artist profile by userId: {}", userId);
        ArtistProfile profile = artistProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile not found for user: " + userId));
        return mapper.toResponse(profile);
    }

    @Override
    public List<ArtistProfileResponse> getAll() {
        log.debug("Fetching all artist profiles");
        return artistProfileRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArtistProfileResponse> getByGenreId(Long genreId) {
        log.debug("Fetching artist profiles by genre: {}", genreId);
        return artistProfileRepository.findByGenreId(genreId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArtistProfileResponse update(Long id, ArtistProfileRequest request) {
        log.info("Updating artist profile id: {}", id);
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
    @Transactional
    public void delete(Long id) {
        log.info("Deleting artist profile id: {}", id);
        if (!artistProfileRepository.existsById(id)) {
            throw new ResourceNotFoundException("ArtistProfile", id);
        }
        artistProfileRepository.deleteById(id);
    }
}
