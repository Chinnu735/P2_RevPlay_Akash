package com.revplay.app.repository;

import com.revplay.app.entity.ListeningHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {
    List<ListeningHistory> findByUserIdOrderByPlayedAtDesc(Long userId);

    List<ListeningHistory> findBySongId(Long songId);

    // Last N songs for a user (paginated)
    List<ListeningHistory> findByUserIdOrderByPlayedAtDesc(Long userId, Pageable pageable);

    // Clear history for a user
    void deleteByUserId(Long userId);

    // Count total plays for songs by a specific artist
    @Query("SELECT COUNT(lh) FROM ListeningHistory lh WHERE lh.song.artist.id = :artistId")
    long countPlaysByArtistId(@Param("artistId") Long artistId);

    // Count plays per song for an artist
    @Query("SELECT lh.song.id, COUNT(lh) FROM ListeningHistory lh WHERE lh.song.artist.id = :artistId GROUP BY lh.song.id ORDER BY COUNT(lh) DESC")
    List<Object[]> countPlaysBySongForArtist(@Param("artistId") Long artistId);

    // Count plays for a specific song
    long countBySongId(Long songId);

    // Top listeners for an artist (userId, playCount)
    @Query("SELECT lh.user.id, COUNT(lh) FROM ListeningHistory lh WHERE lh.song.artist.id = :artistId GROUP BY lh.user.id ORDER BY COUNT(lh) DESC")
    List<Object[]> findTopListenersByArtistId(@Param("artistId") Long artistId, Pageable pageable);

    // Daily play counts for an artist within a date range
    @Query("SELECT CAST(lh.playedAt AS date), COUNT(lh) FROM ListeningHistory lh WHERE lh.song.artist.id = :artistId AND lh.playedAt BETWEEN :startDate AND :endDate GROUP BY CAST(lh.playedAt AS date) ORDER BY CAST(lh.playedAt AS date)")
    List<Object[]> findDailyPlaysByArtistId(@Param("artistId") Long artistId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Total listening time for a user (sum of song durations)
    @Query("SELECT COUNT(lh) FROM ListeningHistory lh WHERE lh.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);
}
