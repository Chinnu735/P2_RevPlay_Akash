package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.SongMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.SongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SongServiceImpl implements SongService {

    private final SongRepository songRepository;
    private final ArtistProfileRepository artistProfileRepository;
    private final AlbumRepository albumRepository;
    private final GenreRepository genreRepository;
    private final ListeningHistoryRepository historyRepository;
    private final FavoriteRepository favoriteRepository;
    private final PlaylistSongRepository playlistSongRepository;
    private final SongMapper mapper;

    @Override
    public SongResponse create(SongRequest request) {
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
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song", id));
        return mapper.toResponse(song);
    }

    @Override
    public List<SongResponse> getAll() {
        return songRepository.findByIsDeleted(0).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongResponse> getByArtistId(Long artistId) {
        return songRepository.findByArtistIdAndIsDeletedNot(artistId, 1).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongResponse> getByAlbumId(Long albumId) {
        return songRepository.findByAlbumId(albumId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SongResponse> getByGenreId(Long genreId) {
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
    public SongResponse update(Long id, SongRequest request) {
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
    @org.springframework.transaction.annotation.Transactional
    public void softDelete(Long id) {
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
