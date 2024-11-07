package com.nurijang.controller;

import com.nurijang.dto.GetFacilitiesRequest;
import com.nurijang.dto.GetFacilitiesResponse;
import com.nurijang.entity.FacilityEntity;
import com.nurijang.repository.FacilityRepository;
import com.nurijang.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityRepository facilityRepository;
    private final FacilityService facilityService;

    @Operation(summary = "시설 상세정보", description = "시설 id를 파라미터에 입력")
    @GetMapping("/{id}")
    public FacilityEntity getFacility(@RequestParam int id) {
        return facilityRepository.findById(id);
    }

    @Operation(summary = "주변 시설 정보", description = "위도 경도 입력")
    @PostMapping("/facilities")
    public List<GetFacilitiesResponse> getFacilities(@RequestBody GetFacilitiesRequest getFacilitiesRequest) {
        return facilityService.getFacilities(getFacilitiesRequest);
    }
}
