package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.AlbumMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final SongRepository songRepository;
    private final com.revplay.app.service.SongService songService;
    private final AlbumMapper mapper;

    @Override
    public AlbumResponse create(AlbumRequest request) {
        ArtistProfile artist = artistProfileRepository.findById(request.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", request.getArtistId()));
        Album album = mapper.toEntity(request, artist);
        return mapper.toResponse(albumRepository.save(album));
    }

    @Override
    public AlbumResponse getById(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album", id));
        return mapper.toResponse(album);
    }

    @Override
    public List<AlbumResponse> getAll() {
        return albumRepository.findByIsDeleted(0).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AlbumResponse> getByArtistId(Long artistId) {
        return albumRepository.findByArtistIdAndIsDeletedNot(artistId, 1).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AlbumResponse update(Long id, AlbumRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Album", id));
        mapper.updateEntity(album, request);
        return mapper.toResponse(albumRepository.save(album));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void softDelete(Long id) {
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
