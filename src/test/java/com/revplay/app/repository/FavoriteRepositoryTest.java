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
class FavoriteRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IFavoriteRepository favoriteRepository;

    private User user;
    private Song song;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("f@test.com");
        user.setUsername("f1");
        user.setPassword("p");
        user.setRole("USER");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        Genre genre = new Genre();
        genre.setName("Pop");
        genre = em.persistAndFlush(genre);

        ArtistProfile artist = new ArtistProfile();
        artist.setUser(user);
        artist.setArtistName("A1");
        artist.setGenre(genre);
        artist = em.persistAndFlush(artist);

        song = new Song();
        song.setArtist(artist);
        song.setGenre(genre);
        song.setTitle("FavSong");
        song.setAudioUrl("http://f.mp3");
        song.setIsDeleted(0);
        song = em.persistAndFlush(song);

        favorite = new Favorite();
        favorite.setUser(user);
        favorite.setSong(song);
        favorite = em.persistAndFlush(favorite);
    }

    @Test
    void findByUserId_ShouldReturnFavorites() {
        List<Favorite> found = favoriteRepository.findByUserId(user.getId());
        assertThat(found).hasSize(1);
    }

    @Test
    void existsByUserIdAndSongId_ShouldReturnTrue() {
        assertThat(favoriteRepository.existsByUserIdAndSongId(user.getId(), song.getId())).isTrue();
    }

    @Test
    void existsByUserIdAndSongId_ShouldReturnFalse() {
        assertThat(favoriteRepository.existsByUserIdAndSongId(user.getId(), 999L)).isFalse();
    }

    @Test
    void countByUserId_ShouldReturnCount() {
        assertThat(favoriteRepository.countByUserId(user.getId())).isEqualTo(1L);
    }
}
