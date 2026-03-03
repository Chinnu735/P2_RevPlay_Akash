package com.revplay.app.repository;

import com.revplay.app.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISongRepository extends JpaRepository<Song, Long> {
    @EntityGraph(attributePaths = { "artist", "album", "genre" })
    List<Song> findByArtistId(Long artistId);

    @EntityGraph(attributePaths = { "artist", "album", "genre" })
    List<Song> findByAlbumId(Long albumId);

    @EntityGraph(attributePaths = { "artist", "album", "genre" })
    List<Song> findByGenreId(Long genreId);

    @EntityGraph(attributePaths = { "artist", "album", "genre" })
    List<Song> findByGenreIdAndIsDeleted(Long genreId, Integer isDeleted);

    @EntityGraph(attributePaths = { "artist", "album", "genre" })
    List<Song> findByArtistIdAndIsDeletedNot(Long artistId, Integer isDeleted);

    @EntityGraph(attributePaths = { "artist", "album", "genre" })
    List<Song> findByIsDeleted(Integer isDeleted);

    @EntityGraph(attributePaths = { "artist", "album", "genre" })
    List<Song> findByTitleContainingIgnoreCase(String title);

    long countByArtistIdAndIsDeleted(Long artistId, Integer isDeleted);

    boolean existsByAlbumId(Long albumId);

    @EntityGraph(attributePaths = { "artist", "album", "genre" })
    List<Song> findByIsDeletedOrderByCreatedAtDesc(Integer isDeleted,
            org.springframework.data.domain.Pageable pageable);
}
