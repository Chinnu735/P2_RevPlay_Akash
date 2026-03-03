package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface IFavoriteService {
    FavoriteResponse addFavorite(FavoriteRequest request);

    void removeFavorite(Long userId, Long songId);

    List<FavoriteResponse> getFavoritesByUserId(Long userId);

    boolean isFavorite(Long userId, Long songId);
}
