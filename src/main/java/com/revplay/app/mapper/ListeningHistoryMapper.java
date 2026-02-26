package com.revplay.app.mapper;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import org.springframework.stereotype.Component;

@Component
public class ListeningHistoryMapper {
    public ListeningHistoryResponse toResponse(ListeningHistory history) {
        return ListeningHistoryResponse.builder()
                .id(history.getId())
                .userId(history.getUser().getId())
                .songId(history.getSong().getId())
                .songTitle(history.getSong().getTitle())
                .artistName(history.getSong().getArtist().getArtistName())
                .playedAt(history.getPlayedAt())
                .build();
    }
}
