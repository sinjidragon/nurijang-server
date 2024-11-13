package com.nurijang.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SuggestionFacilityResponse {
    private int id;
    private Double distance; // 거리
    private String fcltyNm; // 시설명

    public SuggestionFacilityResponse(int id, Double distance, String fcltyNm) {
        this.id = id;
        this.distance = distance;
        this.fcltyNm = fcltyNm;
    }
}
