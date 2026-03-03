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
class PodcastEpisodeRepositoryTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private IPodcastEpisodeRepository episodeRepository;

    private Podcast podcast;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("pe@test.com");
        user.setUsername("pe1");
        user.setPassword("p");
        user.setRole("ARTIST");
        user.setIsActive(1);
        user = em.persistAndFlush(user);

        Genre genre = new Genre();
        genre.setName("Interview");
        genre = em.persistAndFlush(genre);

        ArtistProfile artist = new ArtistProfile();
        artist.setUser(user);
        artist.setArtistName("Host1");
        artist.setGenre(genre);
        artist = em.persistAndFlush(artist);

        podcast = new Podcast();
        podcast.setArtist(artist);
        podcast.setTitle("EpPodcast");
        podcast = em.persistAndFlush(podcast);

        PodcastEpisode ep = new PodcastEpisode();
        ep.setPodcast(podcast);
        ep.setTitle("Episode1");
        ep.setAudioUrl("http://ep1.mp3");
        em.persistAndFlush(ep);
    }

    @Test
    void findByPodcastIdOrderByReleaseDateDesc_ShouldReturnEpisodes() {
        List<PodcastEpisode> found = episodeRepository.findByPodcastIdOrderByReleaseDateDesc(podcast.getId());
        assertThat(found).hasSize(1);
    }

    @Test
    void findByPodcastId_ShouldReturnEmpty_WhenNoMatch() {
        assertThat(episodeRepository.findByPodcastIdOrderByReleaseDateDesc(999L)).isEmpty();
    }
}
