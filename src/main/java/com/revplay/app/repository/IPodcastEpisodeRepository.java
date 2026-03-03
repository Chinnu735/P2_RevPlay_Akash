package com.revplay.app.repository;

import com.revplay.app.entity.PodcastEpisode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPodcastEpisodeRepository extends JpaRepository<PodcastEpisode, Long> {
    List<PodcastEpisode> findByPodcastId(Long podcastId);

    List<PodcastEpisode> findByPodcastIdOrderByReleaseDateDesc(Long podcastId);
}
