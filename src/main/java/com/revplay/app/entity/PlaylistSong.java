package com.revplay.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "playlist_songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSong {

    @EmbeddedId
    private PlaylistSongId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "order_index")
    private Integer orderIndex;
}
