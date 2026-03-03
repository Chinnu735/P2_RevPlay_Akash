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
class AlbumRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IAlbumRepository albumRepository;

    private ArtistProfile artist;
    private Album album;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("a@test.com");
        user.setUsername("a1");
        user.setPassword("p");
        user.setRole("ARTIST");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        Genre genre = new Genre();
        genre.setName("Pop");
        genre = em.persistAndFlush(genre);

        artist = new ArtistProfile();
        artist.setUser(user);
        artist.setArtistName("A1");
        artist.setGenre(genre);
        artist = em.persistAndFlush(artist);

        album = new Album();
        album.setArtist(artist);
        album.setName("Album1");
        album.setIsDeleted(0);
        album = em.persistAndFlush(album);
    }

    @Test
    void findByIsDeleted_ShouldReturnActiveAlbums() {
        List<Album> found = albumRepository.findByIsDeleted(0);
        assertThat(found).hasSize(1);
    }

    @Test
    void findByArtistId_ShouldReturnAlbums() {
        List<Album> found = albumRepository.findByArtistId(artist.getId());
        assertThat(found).hasSize(1);
    }

    @Test
    void findByArtistIdAndIsDeletedNot_ShouldFilter() {
        album.setIsDeleted(1);
        em.persistAndFlush(album);
        assertThat(albumRepository.findByArtistIdAndIsDeletedNot(artist.getId(), 1)).isEmpty();
    }
}
