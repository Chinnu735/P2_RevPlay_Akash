package com.revplay.app.repository;

import com.revplay.app.entity.PlaylistFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPlaylistFollowerRepository extends JpaRepository<PlaylistFollower, Long> {
    List<PlaylistFollower> findByPlaylistId(Long playlistId);

    List<PlaylistFollower> findByUserId(Long userId);

    Optional<PlaylistFollower> findByPlaylistIdAndUserId(Long playlistId, Long userId);

    boolean existsByPlaylistIdAndUserId(Long playlistId, Long userId);

    void deleteByPlaylistIdAndUserId(Long playlistId, Long userId);
}
