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
class PodcastRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IPodcastRepository podcastRepository;

    private ArtistProfile artist;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("pc@test.com");
        user.setUsername("pc1");
        user.setPassword("p");
        user.setRole("ARTIST");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        Genre genre = new Genre();
        genre.setName("Talk");
        genre = em.persistAndFlush(genre);

        artist = new ArtistProfile();
        artist.setUser(user);
        artist.setArtistName("PodcasterA");
        artist.setGenre(genre);
        artist = em.persistAndFlush(artist);

        Podcast podcast = new Podcast();
        podcast.setArtist(artist);
        podcast.setTitle("MyPodcast");
        em.persistAndFlush(podcast);
    }

    @Test
    void findByArtistId_ShouldReturnPodcasts() {
        List<Podcast> found = podcastRepository.findByArtistId(artist.getId());
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getTitle()).isEqualTo("MyPodcast");
    }

    @Test
    void findByArtistId_ShouldReturnEmpty_WhenNoMatch() {
        assertThat(podcastRepository.findByArtistId(999L)).isEmpty();
    }
}
