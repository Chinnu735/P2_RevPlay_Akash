package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.ListeningHistoryMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.ListeningHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListeningHistoryServiceImpl implements ListeningHistoryService {

    private final ListeningHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final ListeningHistoryMapper mapper;

    @Override
    public ListeningHistoryResponse recordPlay(ListeningHistoryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));
        Song song = songRepository.findById(request.getSongId())
                .orElseThrow(() -> new ResourceNotFoundException("Song", request.getSongId()));

        ListeningHistory history = new ListeningHistory();
        history.setUser(user);
        history.setSong(song);
        history.setPlayedAt(LocalDateTime.now());
        return mapper.toResponse(historyRepository.save(history));
    }

    @Override
    public List<ListeningHistoryResponse> getHistoryByUserId(Long userId) {
        return historyRepository.findByUserIdOrderByPlayedAtDesc(userId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListeningHistoryResponse> getRecentHistory(Long userId, int limit) {
        return historyRepository.findAllByUserIdOrderByPlayedAtDesc(userId, PageRequest.of(0, limit))
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clearHistory(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        historyRepository.deleteByUserId(userId);
    }
}
