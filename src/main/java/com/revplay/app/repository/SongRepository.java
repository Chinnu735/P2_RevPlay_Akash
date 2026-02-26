package com.revplay.app.repository;

import com.revplay.app.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByArtistId(Long artistId);

    List<Song> findByAlbumId(Long albumId);

    List<Song> findByGenreId(Long genreId);

    List<Song> findByGenreIdAndIsDeleted(Long genreId, Integer isDeleted);

    List<Song> findByArtistIdAndIsDeletedNot(Long artistId, Integer isDeleted);

    List<Song> findByIsDeleted(Integer isDeleted);

    List<Song> findByTitleContainingIgnoreCase(String title);

    long countByArtistIdAndIsDeleted(Long artistId, Integer isDeleted);

    boolean existsByAlbumId(Long albumId);

    List<Song> findByIsDeletedOrderByCreatedAtDesc(Integer isDeleted,
            org.springframework.data.domain.Pageable pageable);
}
