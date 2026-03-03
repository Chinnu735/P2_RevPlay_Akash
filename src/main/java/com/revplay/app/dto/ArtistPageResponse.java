package com.revplay.app.dto;

import java.util.List;

public class ArtistPageResponse {
    private Long artistProfileId;
    private String artistName;
    private String genreName;
    private String bannerImage;
    private String instagramLink;
    private String youtubeLink;
    private String profileImage;
    private String bio;
    private List<SongResponse> songs;
    private List<AlbumResponse> albums;

    public ArtistPageResponse() {
    }

    public static ArtistPageResponseBuilder builder() {
        return new ArtistPageResponseBuilder();
    }

    public Long getArtistProfileId() {
        return artistProfileId;
    }

    public void setArtistProfileId(Long id) {
        this.artistProfileId = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String name) {
        this.artistName = name;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String name) {
        this.genreName = name;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String image) {
        this.bannerImage = image;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String link) {
        this.instagramLink = link;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String link) {
        this.youtubeLink = link;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<SongResponse> getSongs() {
        return songs;
    }

    public void setSongs(List<SongResponse> songs) {
        this.songs = songs;
    }

    public List<AlbumResponse> getAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumResponse> albums) {
        this.albums = albums;
    }

    public static class ArtistPageResponseBuilder {
        private ArtistPageResponse response = new ArtistPageResponse();

        public ArtistPageResponseBuilder artistProfileId(Long id) {
            response.setArtistProfileId(id);
            return this;
        }

        public ArtistPageResponseBuilder artistName(String name) {
            response.setArtistName(name);
            return this;
        }

        public ArtistPageResponseBuilder genreName(String name) {
            response.setGenreName(name);
            return this;
        }

        public ArtistPageResponseBuilder bannerImage(String image) {
            response.setBannerImage(image);
            return this;
        }

        public ArtistPageResponseBuilder instagramLink(String link) {
            response.setInstagramLink(link);
            return this;
        }

        public ArtistPageResponseBuilder youtubeLink(String link) {
            response.setYoutubeLink(link);
            return this;
        }

        public ArtistPageResponseBuilder profileImage(String image) {
            response.setProfileImage(image);
            return this;
        }

        public ArtistPageResponseBuilder bio(String bio) {
            response.setBio(bio);
            return this;
        }

        public ArtistPageResponseBuilder songs(List<SongResponse> songs) {
            response.setSongs(songs);
            return this;
        }

        public ArtistPageResponseBuilder albums(List<AlbumResponse> albums) {
            response.setAlbums(albums);
            return this;
        }

        public ArtistPageResponse build() {
            return response;
        }
    }
}
