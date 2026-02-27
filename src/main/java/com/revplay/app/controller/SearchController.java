package com.revplay.app.controller;

import com.revplay.app.dto.*;
import com.revplay.app.mapper.*;
import com.revplay.app.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistProfileRepository artistRepository;
    private final GenreRepository genreRepository;

    private final SongMapper songMapper;
    private final AlbumMapper albumMapper;
    private final ArtistProfileMapper artistMapper;
    private final GenreMapper genreMapper;

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam String q) {
        String query = q.trim();

        List<SongResponse> songs = songRepository.findByTitleContainingIgnoreCase(query)
                .stream()
                .filter(s -> s.getIsDeleted() == null || s.getIsDeleted() == 0)
                .map(songMapper::toResponse)
                .collect(Collectors.toList());

        List<AlbumResponse> albums = albumRepository.findByNameContainingIgnoreCaseAndIsDeletedNot(query, 1)
                .stream()
                .map(albumMapper::toResponse)
                .collect(Collectors.toList());

        List<ArtistProfileResponse> artists = artistRepository.findByArtistNameContainingIgnoreCase(query)
                .stream()
                .map(artistMapper::toResponse)
                .collect(Collectors.toList());

        List<GenreResponse> genres = genreRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(genreMapper::toResponse)
                .collect(Collectors.toList());

        SearchResponse response = SearchResponse.builder()
                .songs(songs)
                .albums(albums)
                .artists(artists)
                .genres(genres)
                .build();

        return ResponseEntity.ok(response);
    }
}
