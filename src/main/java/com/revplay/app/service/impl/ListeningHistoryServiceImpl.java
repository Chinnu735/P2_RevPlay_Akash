package com.revplay.app.service.impl;

import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.exception.*;
import com.revplay.app.mapper.ListeningHistoryMapper;
import com.revplay.app.repository.*;
import com.revplay.app.service.IListeningHistoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListeningHistoryServiceImpl implements IListeningHistoryService {
    private static final Logger log = LoggerFactory.getLogger(ListeningHistoryServiceImpl.class);

    private final IListeningHistoryRepository historyRepository;
    private final IUserRepository userRepository;
    private final ISongRepository songRepository;
    private final ListeningHistoryMapper mapper;

    @Override
    @Transactional
    public ListeningHistoryResponse recordPlay(ListeningHistoryRequest request) {
        log.info("Recording play - userId: {}, songId: {}", request.getUserId(), request.getSongId());
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
        log.debug("Fetching listening history for userId: {}", userId);
        return historyRepository.findByUserIdOrderByPlayedAtDesc(userId).stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ListeningHistoryResponse> getRecentHistory(Long userId, int limit) {
        log.debug("Fetching recent {} history entries for userId: {}", limit, userId);
        return historyRepository.findAllByUserIdOrderByPlayedAtDesc(userId, PageRequest.of(0, limit))
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clearHistory(Long userId) {
        log.info("Clearing listening history for userId: {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        historyRepository.deleteByUserId(userId);
    }
}
