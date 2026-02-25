package com.revplay.app.repository;

import com.revplay.app.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findByUserIdAndIsDeletedNot(Long userId, Integer isDeleted);

    List<Playlist> findByUserId(Long userId);

    List<Playlist> findByPrivacy(String privacy);

    // Public playlists that are not deleted
    List<Playlist> findByPrivacyAndIsDeleted(String privacy, Integer isDeleted);

    // Count playlists for a user
    long countByUserIdAndIsDeleted(Long userId, Integer isDeleted);
}
