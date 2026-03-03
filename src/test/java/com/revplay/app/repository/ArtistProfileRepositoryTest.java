package com.revplay.app.repository;

import com.revplay.app.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class ArtistProfileRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IArtistProfileRepository artistProfileRepository;

    private User user;
    private Genre genre;
    private ArtistProfile profile;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("artist@test.com");
        user.setUsername("artist1");
        user.setPassword("pass");
        user.setRole("ARTIST");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        genre = new Genre();
        genre.setName("Rock");
        genre = em.persistAndFlush(genre);

        profile = new ArtistProfile();
        profile.setUser(user);
        profile.setArtistName("Artist1");
        profile.setGenre(genre);
        profile = em.persistAndFlush(profile);
    }

    @Test
    void findByUserId_ShouldReturnProfile() {
        assertThat(artistProfileRepository.findByUserId(user.getId())).isPresent();
    }

    @Test
    void findByGenreId_ShouldReturnList() {
        List<ArtistProfile> result = artistProfileRepository.findByGenreId(genre.getId());
        assertThat(result).hasSize(1);
    }

    @Test
    void existsByUserId_ShouldReturnTrue() {
        assertThat(artistProfileRepository.existsByUserId(user.getId())).isTrue();
    }

    @Test
    void existsByUserId_ShouldReturnFalse() {
        assertThat(artistProfileRepository.existsByUserId(999L)).isFalse();
    }
}
