package com.revplay.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_seq")
    @SequenceGenerator(name = "genre_seq", sequenceName = "genres_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
}
