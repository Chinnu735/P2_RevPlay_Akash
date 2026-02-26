package com.revplay.app.repository;

import com.revplay.app.entity.PlaylistSong;
import com.revplay.app.entity.PlaylistSongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {
    List<PlaylistSong> findByPlaylistIdOrderByOrderIndexAsc(Long playlistId);

    void deleteBySongId(Long songId);

    void deleteByPlaylistIdAndSongId(Long playlistId, Long songId);

    boolean existsByPlaylistIdAndSongId(Long playlistId, Long songId);

    long countByPlaylistId(Long playlistId);
}
