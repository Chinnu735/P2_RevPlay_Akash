package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.PlaylistSongMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.PlaylistSongService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistSongServiceImpl implements PlaylistSongService {

    private final PlaylistSongRepository playlistSongRepository;
    private final PlaylistRepository playlistRepository;
    private final SongRepository songRepository;
    private final PlaylistSongMapper mapper;

    @Override
    public PlaylistSongResponse addSongToPlaylist(PlaylistSongRequest request) {
        if (playlistSongRepository.existsByPlaylistIdAndSongId(request.getPlaylistId(), request.getSongId())) {
            throw new DuplicateResourceException("Song already in playlist");
        }
        Playlist playlist = playlistRepository.findById(request.getPlaylistId())
                .orElseThrow(() -> new ResourceNotFoundException("Playlist", request.getPlaylistId()));
        Song song = songRepository.findById(request.getSongId())
                .orElseThrow(() -> new ResourceNotFoundException("Song", request.getSongId()));

        PlaylistSongId id = new PlaylistSongId(playlist.getId(), song.getId());
        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setId(id);
        playlistSong.setPlaylist(playlist);
        playlistSong.setSong(song);

        Integer orderIndex = request.getOrderIndex();
        if (orderIndex == null) {
            orderIndex = (int) playlistSongRepository.countByPlaylistId(request.getPlaylistId()) + 1;
        }
        playlistSong.setOrderIndex(orderIndex);

        return mapper.toResponse(playlistSongRepository.save(playlistSong));
    }

    @Override
    @Transactional
    public void removeSongFromPlaylist(Long playlistId, Long songId) {
        PlaylistSongId id = new PlaylistSongId(playlistId, songId);
        if (!playlistSongRepository.existsById(id)) {
            throw new ResourceNotFoundException("Song not found in playlist");
        }
        playlistSongRepository.deleteById(id);
    }

    @Override
    public List<PlaylistSongResponse> getSongsByPlaylistId(Long playlistId) {
        return playlistSongRepository.findByPlaylistIdOrderByOrderIndexAsc(playlistId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void reorderSong(Long playlistId, Long songId, Integer newOrderIndex) {
        PlaylistSongId id = new PlaylistSongId(playlistId, songId);
        PlaylistSong playlistSong = playlistSongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Song not found in playlist"));
        playlistSong.setOrderIndex(newOrderIndex);
        playlistSongRepository.save(playlistSong);
    }
}
