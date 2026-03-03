package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.AlbumMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IAlbumService;
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
public class AlbumServiceImpl implements IAlbumService {
    private static final Logger log = LoggerFactory.getLogger(AlbumServiceImpl.class);

    private final IAlbumRepository albumRepository;
    private final IArtistProfileRepository artistProfileRepository;
    private final ISongRepository songRepository;
    private final com.revplay.app.service.ISongService songService;
    private final AlbumMapper mapper;

    @Override
    @Transactional
    public AlbumResponse create(AlbumRequest request) {
        log.info("Creating album: {} for artist: {}", request.getName(), request.getArtistId());
        ArtistProfile artist = artistProfileRepository.findById(request.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", request.getArtistId()));
        Album album = mapper.toEntity(request, artist);
        return mapper.toResponse(albumRepository.save(album));
    }

    @Override
    public AlbumResponse getById(Long id) {
        log.debug("Fetching album by id: {}", id);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album", id));
        return mapper.toResponse(album);
    }

    @Override
    public List<AlbumResponse> getAll() {
        log.debug("Fetching all albums");
        return albumRepository.findByIsDeleted(0).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlbumResponse> getByArtistId(Long artistId) {
        log.debug("Fetching albums by artist id: {}", artistId);
        return albumRepository.findByArtistIdAndIsDeletedNot(artistId, 1).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AlbumResponse update(Long id, AlbumRequest request) {
        log.info("Updating album id: {}", id);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album", id));
        mapper.updateEntity(album, request);
        return mapper.toResponse(albumRepository.save(album));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void softDelete(Long id) {
        log.info("Soft-deleting album id: {}", id);
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album", id));

        // Cascade delete explicitly logic utilizing the SongService functionality
        List<Song> albumSongs = songRepository.findByAlbumId(id);
        for (Song song : albumSongs) {
            songService.softDelete(song.getId());
        }

        album.setIsDeleted(1);
        albumRepository.save(album);
    }
}
