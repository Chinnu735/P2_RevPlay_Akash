package com.revplay.app.repository;

import com.revplay.app.entity.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IGenreRepository genreRepository;

    private Genre genre;

    @BeforeEach
    void setUp() {
        genre = new Genre();
        genre.setName("Rock");
        genre = em.persistAndFlush(genre);
    }

    @Test
    void findByName_ShouldReturnGenre() {
        Optional<Genre> found = genreRepository.findByName("Rock");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Rock");
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenNotFound() {
        assertThat(genreRepository.findByName("Jazz")).isEmpty();
    }

    @Test
    void existsByName_ShouldReturnTrue() {
        assertThat(genreRepository.existsByName("Rock")).isTrue();
    }

    @Test
    void existsByName_ShouldReturnFalse() {
        assertThat(genreRepository.existsByName("Classical")).isFalse();
    }
}
