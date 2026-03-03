package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.SongMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.ISongService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SongServiceImpl implements ISongService {
    private static final Logger log = LoggerFactory.getLogger(SongServiceImpl.class);

    private final ISongRepository songRepository;
    private final IArtistProfileRepository artistProfileRepository;
    private final IAlbumRepository albumRepository;
    private final IGenreRepository genreRepository;
    private final IListeningHistoryRepository historyRepository;
    private final IFavoriteRepository favoriteRepository;
    private final IPlaylistSongRepository playlistSongRepository;
    private final SongMapper mapper;

    @Override
    @Transactional
    public SongResponse create(SongRequest request) {
        log.info("Creating song: {} by artist: {}", request.getTitle(), request.getArtistId());
        ArtistProfile artist = artistProfileRepository.findById(request.getArtistId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtistProfile", request.getArtistId()));
        Album album = null;
        if (request.getAlbumId() != null) {
            album = albumRepository.findById(request.getAlbumId())
                    .orElseThrow(() -> new ResourceNotFoundException("Album", request.getAlbumId()));
        }
        Genre genre = null;
        if (request.getGenreId() != null) {
            genre = genreRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre", request.getGenreId()));
        }
        Song song = mapper.toEntity(request, artist, album, genre);
        return mapper.toResponse(songRepository.save(song));
    }

    @Override
    public SongResponse getById(Long id) {
        log.debug("Fetching song by id: {}", id);
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", id));
        return mapper.toResponse(song);
    }

    @Override
    public List<SongResponse> getAll() {
        log.debug("Fetching all songs");
        return songRepository.findByIsDeleted(0).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongResponse> getByArtistId(Long artistId) {
        log.debug("Fetching songs by artist id: {}", artistId);
        return songRepository.findByArtistIdAndIsDeletedNot(artistId, 1).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongResponse> getByAlbumId(Long albumId) {
        log.debug("Fetching songs by album id: {}", albumId);
        return songRepository.findByAlbumId(albumId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongResponse> getByGenreId(Long genreId) {
        log.debug("Fetching songs by genre id: {}", genreId);
        return songRepository.findByGenreIdAndIsDeleted(genreId, 0).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongResponse> getSongsByGenre(Long genreId) {
        return getByGenreId(genreId);
    }

    @Override
    public List<SongResponse> search(String title) {
        log.debug("Searching songs by title: {}", title);
        return songRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongResponse> getTrendingSongs() {
        return historyRepository.findTrendingSongsQuery(PageRequest.of(0, 10))
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SongResponse update(Long id, SongRequest request) {
        log.info("Updating song id: {}", id);
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", id));
        Album album = null;
        if (request.getAlbumId() != null) {
            album = albumRepository.findById(request.getAlbumId())
                    .orElseThrow(() -> new ResourceNotFoundException("Album", request.getAlbumId()));
        }
        Genre genre = null;
        if (request.getGenreId() != null) {
            genre = genreRepository.findById(request.getGenreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Genre", request.getGenreId()));
        }
        mapper.updateEntity(song, request, album, genre);
        return mapper.toResponse(songRepository.save(song));
    }

    @Override
    @Transactional
    public void updateDuration(Long id, Integer duration) {
        log.info("Updating duration for song id: {} to {}", id, duration);
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", id));
        song.setDuration(duration);
        songRepository.save(song);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void softDelete(Long id) {
        log.info("Soft-deleting song id: {}", id);
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", id));

        // Cascading Deletes to explicitly remove the song from user associations
        favoriteRepository.deleteBySongId(id);
        playlistSongRepository.deleteBySongId(id);
        historyRepository.deleteBySongId(id);

        song.setIsDeleted(1);
        songRepository.save(song);
    }

    @Override
    public List<SongResponse> getRecentSongs() {
        return songRepository.findByIsDeletedOrderByCreatedAtDesc(0, PageRequest.of(0, 10))
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
