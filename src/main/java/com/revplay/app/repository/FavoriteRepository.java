package com.revplay.app.repository;

import com.revplay.app.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);

    Optional<Favorite> findByUserIdAndSongId(Long userId, Long songId);

    boolean existsByUserIdAndSongId(Long userId, Long songId);

    void deleteByUserIdAndSongId(Long userId, Long songId);

    // Count total favorites for an artist's songs
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.song.artist.id = :artistId")
    long countFavoritesByArtistId(@Param("artistId") Long artistId);

    // Count favorites for a user
    long countByUserId(Long userId);

    // Find users who favorited an artist's songs
    @Query("SELECT f FROM Favorite f WHERE f.song.artist.id = :artistId ORDER BY f.addedAt DESC")
    List<Favorite> findFavoritesByArtistSongs(@Param("artistId") Long artistId);
}
