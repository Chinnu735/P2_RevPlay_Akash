package com.revplay.app.dto;

import java.util.List;

public class AlbumDetailResponse {
    private Long albumId;
    private String albumName;
    private String description;
    private String coverImageUrl;
    private String releaseDate;
    private String artistName;
    private Long artistId;
    private List<SongResponse> tracks;

    public AlbumDetailResponse() {
    }

    public static AlbumDetailResponseBuilder builder() {
        return new AlbumDetailResponseBuilder();
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long id) {
        this.albumId = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String name) {
        this.albumName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String url) {
        this.coverImageUrl = url;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String date) {
        this.releaseDate = date;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String name) {
        this.artistName = name;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long id) {
        this.artistId = id;
    }

    public List<SongResponse> getTracks() {
        return tracks;
    }

    public void setTracks(List<SongResponse> tracks) {
        this.tracks = tracks;
    }

    public static class AlbumDetailResponseBuilder {
        private AlbumDetailResponse response = new AlbumDetailResponse();

        public AlbumDetailResponseBuilder albumId(Long id) {
            response.setAlbumId(id);
            return this;
        }

        public AlbumDetailResponseBuilder albumName(String name) {
            response.setAlbumName(name);
            return this;
        }

        public AlbumDetailResponseBuilder description(String desc) {
            response.setDescription(desc);
            return this;
        }

        public AlbumDetailResponseBuilder coverImageUrl(String url) {
            response.setCoverImageUrl(url);
            return this;
        }

        public AlbumDetailResponseBuilder releaseDate(String date) {
            response.setReleaseDate(date);
            return this;
        }

        public AlbumDetailResponseBuilder artistName(String name) {
            response.setArtistName(name);
            return this;
        }

        public AlbumDetailResponseBuilder artistId(Long id) {
            response.setArtistId(id);
            return this;
        }

        public AlbumDetailResponseBuilder tracks(List<SongResponse> tracks) {
            response.setTracks(tracks);
            return this;
        }

        public AlbumDetailResponse build() {
            return response;
        }
    }
}
