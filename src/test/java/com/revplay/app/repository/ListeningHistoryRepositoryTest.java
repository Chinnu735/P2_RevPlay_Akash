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
class ListeningHistoryRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IListeningHistoryRepository historyRepository;

    private User user;
    private Song song;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("h@test.com");
        user.setUsername("h1");
        user.setPassword("p");
        user.setRole("USER");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        Genre genre = new Genre();
        genre.setName("Jazz");
        genre = em.persistAndFlush(genre);

        ArtistProfile artist = new ArtistProfile();
        artist.setUser(user);
        artist.setArtistName("A1");
        artist.setGenre(genre);
        artist = em.persistAndFlush(artist);

        song = new Song();
        song.setArtist(artist);
        song.setGenre(genre);
        song.setTitle("HitSong");
        song.setAudioUrl("http://h.mp3");
        song.setIsDeleted(0);
        song = em.persistAndFlush(song);

        ListeningHistory history = new ListeningHistory();
        history.setUser(user);
        history.setSong(song);
        em.persistAndFlush(history);
    }

    @Test
    void findByUserIdOrderByPlayedAtDesc_ShouldReturnHistory() {
        assertThat(historyRepository.findByUserIdOrderByPlayedAtDesc(user.getId())).hasSize(1);
    }

    @Test
    void countByUserId_ShouldReturnCount() {
        assertThat(historyRepository.countByUserId(user.getId())).isEqualTo(1L);
    }

    @Test
    void countBySongId_ShouldReturnCount() {
        assertThat(historyRepository.countBySongId(song.getId())).isEqualTo(1L);
    }
}
