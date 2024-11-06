package com.nurijang.controller;

import com.nurijang.entity.FacilityEntity;
import com.nurijang.repository.FacilityRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityRepository facilityRepository;

    @Operation(summary = "시설 상세정보", description = "시설 id를 파라미터에 입력")
    @GetMapping("/{id}")
    public FacilityEntity getFacility(@RequestParam int id) {
        return facilityRepository.findById(id);
    }
}
