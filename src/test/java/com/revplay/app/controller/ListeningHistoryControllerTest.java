package com.revplay.app.controller;

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
public class ListeningHistoryControllerTest {

    @Mock
    private IListeningHistoryService historyService;

    @InjectMocks
    private ListeningHistoryController target;

}
