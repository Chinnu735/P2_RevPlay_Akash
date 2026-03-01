package com.revplay.app.config;

import com.revplay.app.entity.Genre;
import com.revplay.app.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final GenreRepository genreRepository;

    @Override
    public void run(String... args) throws Exception {
        if (genreRepository.count() == 0) {
            List<Genre> defaultGenres = List.of(
                    new Genre(null, "Rock"),
                    new Genre(null, "Pop"),
                    new Genre(null, "Jazz"),
                    new Genre(null, "Love"),
                    new Genre(null, "Classical"),
                    new Genre(null, "Country"),
                    new Genre(null, "Hip-Hop"));
            genreRepository.saveAll(defaultGenres);
            System.out.println("Default genres seeded into database.");
        }
    }
}
