package com.revplay.app.repository;

import com.revplay.app.entity.ArtistProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {
    Optional<ArtistProfile> findByUserId(Long userId);

    List<ArtistProfile> findByGenreId(Long genreId);

    boolean existsByUserId(Long userId);
}
