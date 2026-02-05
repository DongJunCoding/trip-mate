package com.server.backend.api.v1.app.service;

import com.server.backend.common.data.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository travelRepository;


}
