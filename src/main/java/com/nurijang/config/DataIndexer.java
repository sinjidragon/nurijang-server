package com.nurijang.config;

import com.nurijang.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataIndexer implements CommandLineRunner {
    private final FacilityService facilityService;

    @Override
    public void run(String... args) throws Exception {
        facilityService.indexData();
    }
}
