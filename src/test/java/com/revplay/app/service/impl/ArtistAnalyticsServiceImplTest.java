package com.revplay.app.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.*;
import java.time.*;
import org.springframework.web.multipart.MultipartFile;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import com.revplay.app.service.*;
import com.revplay.app.dto.*;
import com.revplay.app.entity.*;
import com.revplay.app.repository.*;
import com.revplay.app.mapper.*;

@ExtendWith(MockitoExtension.class)
public class ArtistAnalyticsServiceImplTest {

    @Mock
    private IArtistProfileRepository artistProfileRepository;

    @Mock
    private ISongRepository songRepository;

    @Mock
    private IAlbumRepository albumRepository;

    @Mock
    private IListeningHistoryRepository listeningHistoryRepository;

    @Mock
    private IFavoriteRepository favoriteRepository;

    @Mock
    private IPlaylistRepository playlistRepository;

    @Mock
    private IPodcastRepository podcastRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private SongMapper songMapper;

    @Mock
    private AlbumMapper albumMapper;

    @Mock
    private PodcastMapper podcastMapper;

    @InjectMocks
    private ArtistAnalyticsServiceImpl target;

}
