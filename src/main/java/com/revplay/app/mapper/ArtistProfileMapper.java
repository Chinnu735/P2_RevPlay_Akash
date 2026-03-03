package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ArtistProfileMapper {
    public ArtistProfileResponse toResponse(ArtistProfile profile) {
        return ArtistProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .artistName(profile.getArtistName())
                .genreId(profile.getGenre() != null ? profile.getGenre().getId() : null)
                .genreName(profile.getGenre() != null ? profile.getGenre().getName() : null)
                .bannerImage(profile.getBannerImage())
                .instagramLink(profile.getInstagramLink())
                .youtubeLink(profile.getYoutubeLink())
                .createdAt(profile.getCreatedAt())
                .build();
    }

    public ArtistProfile toEntity(ArtistProfileRequest request, User user, Genre genre) {
        ArtistProfile profile = new ArtistProfile();
        profile.setUser(user);
        profile.setArtistName(request.getArtistName());
        profile.setGenre(genre);
        profile.setBannerImage(request.getBannerImage());
        profile.setInstagramLink(request.getInstagramLink());
        profile.setYoutubeLink(request.getYoutubeLink());
        return profile;
    }

    public void updateEntity(ArtistProfile profile, ArtistProfileRequest request, Genre genre) {
        profile.setArtistName(request.getArtistName());
        profile.setGenre(genre);
        profile.setBannerImage(request.getBannerImage());
        profile.setInstagramLink(request.getInstagramLink());
        profile.setYoutubeLink(request.getYoutubeLink());
    }
}
