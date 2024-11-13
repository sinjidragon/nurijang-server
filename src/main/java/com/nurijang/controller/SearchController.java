package com.nurijang.controller;

import com.nurijang.dto.response.GetFacilitiesResponse;
import com.nurijang.dto.request.SearchFacilitiesRequest;
import com.nurijang.dto.response.SearchFacilitiesResponse;
import com.nurijang.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Search", description = "검색 기능")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class SearchController {
    private final FacilityService facilityService;

    @Operation(summary = "검색", description = "경도 위도 검색어 입력")
    @PostMapping("/search")
    public List<GetFacilitiesResponse> searchFacilities(@RequestBody SearchFacilitiesRequest searchFacilitiesRequest) {
        return facilityService.searchFacilities(searchFacilitiesRequest);
    }

    @Operation(summary = "검색어 자동완성")
    @PostMapping("/suggestions")
    public SearchFacilitiesResponse getAutocompleteSuggestions(@RequestBody SearchFacilitiesRequest searchFacilitiesRequest) {
        return facilityService.getAutocompleteSuggestions(searchFacilitiesRequest);
    }
}
