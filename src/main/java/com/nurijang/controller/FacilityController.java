package com.nurijang.controller;

import com.nurijang.dto.GetFacilitiesRequest;
import com.nurijang.dto.GetFacilitiesResponse;
import com.nurijang.entity.FacilityEntity;
import com.nurijang.repository.FacilityRepository;
import com.nurijang.service.FacilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Facility", description = "마크 관련 API")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityRepository facilityRepository;
    private final FacilityService facilityService;

    @Operation(summary = "시설 상세정보", description = "시설 id를 파라미터에 입력" +
            "       \n" +
            "\n" +
            "      ctprvnCd 시도코드\n" +
            "      ctprvnNm 시도명\n" +
            "      signguCd 시군구코드\n" +
            "      signguNm 시군구명\n" +
            "      fcltyNm 시설명\n" +
            "      rprsntvTelNo 대표자전화번호\n" +
            "      zipNo 우편번호\n" +
            "      fcltyAddr 시설주소\n" +
            "      fcltyDetailAddr 시설상세주소\n" +
            "      mainItemCd 주요종목코드\n" +
            "      mainItemNm 주요종목명\n" +
            "      fcltyCrdntLo 시설좌표경도\n" +
            "      fcltyCrdntLa 시설좌표위도\n" +
            "      courseFlagCd 강좌구분코드")
    @GetMapping("/{id}")
    public FacilityEntity getFacility(@RequestParam int id) {
        return facilityRepository.findById(id);
    }

    @Operation(summary = "주변 시설 정보", description = "경도 위도 순으로 입력")
    @PostMapping("/facilities")
    public List<GetFacilitiesResponse> getFacilities(@RequestBody GetFacilitiesRequest request) {
        return facilityService.getFacilities(request.getFcltyCrdntLa(), request.getFcltyCrdntLo());
    }
}
