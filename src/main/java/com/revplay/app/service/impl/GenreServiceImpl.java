package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.Genre;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.GenreMapper;
import com.revplay.app.repository.IGenreRepository;
import com.revplay.app.service.IGenreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements IGenreService {
    private static final Logger log = LoggerFactory.getLogger(GenreServiceImpl.class);

    private final IGenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public GenreResponse create(GenreRequest request) {
        log.info("Creating genre: {}", request.getName());
        if (genreRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException("Genre already exists: " + request.getName());
        }
        Genre genre = genreMapper.toEntity(request);
        return genreMapper.toResponse(genreRepository.save(genre));
    }

    @Override
    public GenreResponse getById(Long id) {
        log.debug("Fetching genre by id: {}", id);
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
        return genreMapper.toResponse(genre);
    }

    @Override
    public List<GenreResponse> getAll() {
        log.debug("Fetching all genres");
        return genreRepository.findAll().stream()
                .map(genreMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GenreResponse update(Long id, GenreRequest request) {
        log.info("Updating genre id: {}", id);
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre", id));
        genre.setName(request.getName());
        return genreMapper.toResponse(genreRepository.save(genre));
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting genre id: {}", id);
        if (!genreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genre", id);
        }
        genreRepository.deleteById(id);
    }
}
