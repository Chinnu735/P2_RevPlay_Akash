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
class PlaylistSongRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IPlaylistSongRepository playlistSongRepository;

    private Playlist playlist;
    private Song song;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("ps@test.com");
        user.setUsername("ps1");
        user.setPassword("p");
        user.setRole("USER");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        Genre genre = new Genre();
        genre.setName("Rock");
        genre = em.persistAndFlush(genre);

        ArtistProfile artist = new ArtistProfile();
        artist.setUser(user);
        artist.setArtistName("A1");
        artist.setGenre(genre);
        artist = em.persistAndFlush(artist);

        song = new Song();
        song.setArtist(artist);
        song.setGenre(genre);
        song.setTitle("S1");
        song.setAudioUrl("http://s.mp3");
        song.setIsDeleted(0);
        song = em.persistAndFlush(song);

        playlist = new Playlist();
        playlist.setUser(user);
        playlist.setName("PL1");
        playlist.setIsDeleted(0);
        playlist.setPrivacy("public");
        playlist = em.persistAndFlush(playlist);

        PlaylistSong ps = new PlaylistSong();
        ps.setId(new PlaylistSongId(playlist.getId(), song.getId()));
        ps.setPlaylist(playlist);
        ps.setSong(song);
        ps.setOrderIndex(1);
        em.persistAndFlush(ps);
    }

    @Test
    void findByPlaylistIdOrderByOrderIndexAsc_ShouldReturnSongs() {
        List<PlaylistSong> found = playlistSongRepository.findByPlaylistIdOrderByOrderIndexAsc(playlist.getId());
        assertThat(found).hasSize(1);
    }

    @Test
    void existsByPlaylistIdAndSongId_ShouldReturnTrue() {
        assertThat(playlistSongRepository.existsByPlaylistIdAndSongId(playlist.getId(), song.getId())).isTrue();
    }

    @Test
    void countByPlaylistId_ShouldReturnCount() {
        assertThat(playlistSongRepository.countByPlaylistId(playlist.getId())).isEqualTo(1L);
    }
}
