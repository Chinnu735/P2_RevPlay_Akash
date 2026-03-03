package com.revplay.app.repository;

import com.revplay.app.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAlbumRepository extends JpaRepository<Album, Long> {
    @Override
    @EntityGraph(attributePaths = { "artist" })
    Optional<Album> findById(Long id);

    @EntityGraph(attributePaths = { "artist" })
    List<Album> findByArtistIdAndIsDeletedNot(Long artistId, Integer isDeleted);

    @EntityGraph(attributePaths = { "artist" })
    List<Album> findByArtistId(Long artistId);

    @EntityGraph(attributePaths = { "artist" })
    List<Album> findByIsDeleted(Integer isDeleted);
}
