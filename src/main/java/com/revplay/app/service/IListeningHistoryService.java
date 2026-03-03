package com.revplay.app.service;

import com.revplay.app.dto.*;
import java.util.List;

public interface IListeningHistoryService {
    ListeningHistoryResponse recordPlay(ListeningHistoryRequest request);

    List<ListeningHistoryResponse> getHistoryByUserId(Long userId);

    List<ListeningHistoryResponse> getRecentHistory(Long userId, int limit);

    void clearHistory(Long userId);
}
