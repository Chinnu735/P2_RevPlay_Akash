package com.revplay.app.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revplay.app.dto.*;
import com.revplay.app.entity.Genre;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.GenreMapper;
import com.revplay.app.repository.GenreRepository;
import com.revplay.app.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private static final Logger logger = LogManager.getLogger(GenreServiceImpl.class);
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreResponse create(GenreRequest request) {
        if (genreRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Genre already exists: " + request.getName());
        }
        Genre genre = genreMapper.toEntity(request);
        return genreMapper.toResponse(genreRepository.save(genre));
    }

    @Override
    public GenreResponse getById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
        return genreMapper.toResponse(genre);
    }

    @Override
    public List<GenreResponse> getAll() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GenreResponse update(Long id, GenreRequest request) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
        genre.setName(request.getName());
        return genreMapper.toResponse(genreRepository.save(genre));
    }

    @Override
    public void delete(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre", id);
        }
        logger.info("Deleting Genre with id: {}", id);
        genreRepository.deleteById(id);
    }
}
