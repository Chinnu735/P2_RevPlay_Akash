package com.revplay.app.config;

import com.revplay.app.entity.Genre;
import com.revplay.app.repository.IGenreRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final IGenreRepository genreRepository;

    @Override
    public void run(String... args) throws Exception {
        if (genreRepository.count() == 0) {
            List<Genre> defaultGenres = List.of(
                    new Genre(null, "Rock"),
                    new Genre(null, "Pop"),
                    new Genre(null, "Jazz"),
                    new Genre(null, "Love"),
                    new Genre(null, "Classical"),
                    new Genre(null, "Country"));
            genreRepository.saveAll(defaultGenres);
            log.info("Default genres seeded into database.");
        }
    }
}
