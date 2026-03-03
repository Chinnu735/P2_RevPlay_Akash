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
class SongRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private ISongRepository songRepository;

    private ArtistProfile artist;
    private Genre genre;
    private Album album;
    private Song song;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("s@test.com");
        user.setUsername("s1");
        user.setPassword("p");
        user.setRole("ARTIST");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        genre = new Genre();
        genre.setName("Rock");
        genre = em.persistAndFlush(genre);

        artist = new ArtistProfile();
        artist.setUser(user);
        artist.setArtistName("A1");
        artist.setGenre(genre);
        artist = em.persistAndFlush(artist);

        album = new Album();
        album.setArtist(artist);
        album.setName("Alb1");
        album.setIsDeleted(0);
        album = em.persistAndFlush(album);

        song = new Song();
        song.setArtist(artist);
        song.setAlbum(album);
        song.setGenre(genre);
        song.setTitle("Song1");
        song.setAudioUrl("http://s.mp3");
        song.setIsDeleted(0);
        song = em.persistAndFlush(song);
    }

    @Test
    void findByIsDeleted_ShouldReturnActiveSongs() {
        assertThat(songRepository.findByIsDeleted(0)).hasSize(1);
    }

    @Test
    void findByArtistIdAndIsDeletedNot_ShouldReturnSongs() {
        assertThat(songRepository.findByArtistIdAndIsDeletedNot(artist.getId(), 1)).hasSize(1);
    }

    @Test
    void findByAlbumId_ShouldReturnSongs() {
        assertThat(songRepository.findByAlbumId(album.getId())).hasSize(1);
    }

    @Test
    void findByGenreIdAndIsDeleted_ShouldReturnSongs() {
        assertThat(songRepository.findByGenreIdAndIsDeleted(genre.getId(), 0)).hasSize(1);
    }

    @Test
    void findByTitleContainingIgnoreCase_ShouldReturnMatching() {
        assertThat(songRepository.findByTitleContainingIgnoreCase("song")).hasSize(1);
        assertThat(songRepository.findByTitleContainingIgnoreCase("xyz")).isEmpty();
    }

    @Test
    void countByArtistIdAndIsDeleted_ShouldReturnCount() {
        assertThat(songRepository.countByArtistIdAndIsDeleted(artist.getId(), 0)).isEqualTo(1L);
    }
}
