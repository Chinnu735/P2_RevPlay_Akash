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
class PlaylistFollowerRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IPlaylistFollowerRepository followerRepository;

    private Playlist playlist;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("pf@test.com");
        user.setUsername("pf1");
        user.setPassword("p");
        user.setRole("USER");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        playlist = new Playlist();
        playlist.setUser(user);
        playlist.setName("PL1");
        playlist.setIsDeleted(0);
        playlist.setPrivacy("public");
        playlist = em.persistAndFlush(playlist);

        PlaylistFollower follower = new PlaylistFollower();
        follower.setPlaylist(playlist);
        follower.setUser(user);
        em.persistAndFlush(follower);
    }

    @Test
    void findByPlaylistId_ShouldReturnFollowers() {
        assertThat(followerRepository.findByPlaylistId(playlist.getId())).hasSize(1);
    }

    @Test
    void findByUserId_ShouldReturnFollowedPlaylists() {
        assertThat(followerRepository.findByUserId(user.getId())).hasSize(1);
    }

    @Test
    void existsByPlaylistIdAndUserId_ShouldReturnTrue() {
        assertThat(followerRepository.existsByPlaylistIdAndUserId(playlist.getId(), user.getId())).isTrue();
    }

    @Test
    void existsByPlaylistIdAndUserId_ShouldReturnFalse() {
        assertThat(followerRepository.existsByPlaylistIdAndUserId(playlist.getId(), 999L)).isFalse();
    }
}
