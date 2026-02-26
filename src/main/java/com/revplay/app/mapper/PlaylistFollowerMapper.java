package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class PlaylistFollowerMapper {
    public PlaylistFollowerResponse toResponse(PlaylistFollower follower) {
        return PlaylistFollowerResponse.builder()
                .id(follower.getId())
                .playlistId(follower.getPlaylist().getId())
                .playlistName(follower.getPlaylist().getName())
                .userId(follower.getUser().getId())
                .username(follower.getUser().getUsername())
                .followedAt(follower.getFollowedAt())
                .build();
    }
}
