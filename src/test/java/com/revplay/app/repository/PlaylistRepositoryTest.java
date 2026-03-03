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
class PlaylistRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IPlaylistRepository playlistRepository;

    private User user;
    private Playlist playlist;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("p@test.com");
        user.setUsername("p1");
        user.setPassword("p");
        user.setRole("USER");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        playlist = new Playlist();
        playlist.setUser(user);
        playlist.setName("MyPlaylist");
        playlist.setIsDeleted(0);
        playlist.setPrivacy("public");
        playlist = em.persistAndFlush(playlist);
    }

    @Test
    void findByUserIdAndIsDeletedNot_ShouldReturnPlaylists() {
        assertThat(playlistRepository.findByUserIdAndIsDeletedNot(user.getId(), 1)).hasSize(1);
    }

    @Test
    void findByPrivacyAndIsDeleted_ShouldReturnPublicPlaylists() {
        assertThat(playlistRepository.findByPrivacyAndIsDeleted("public", 0)).hasSize(1);
    }

    @Test
    void countByUserIdAndIsDeleted_ShouldReturnCount() {
        assertThat(playlistRepository.countByUserIdAndIsDeleted(user.getId(), 0)).isEqualTo(1L);
    }
}
