package com.revplay.app.repository;

import com.revplay.app.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByArtistIdAndIsDeletedNot(Long artistId, Integer isDeleted);

    List<Album> findByArtistId(Long artistId);

    List<Album> findByIsDeleted(Integer isDeleted);
}
