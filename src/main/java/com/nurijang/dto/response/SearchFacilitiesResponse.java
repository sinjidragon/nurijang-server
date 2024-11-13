package com.nurijang.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SearchFacilitiesResponse {
    private List<String> mainItems; // 추천된 주요 종목명
    private List<SuggestionFacilityResponse> facilities; // 추천된 시설 정보

    public SearchFacilitiesResponse(List<String> mainItems, List<SuggestionFacilityResponse> facilities) {
        this.mainItems = mainItems;
        this.facilities = facilities;
    }
}
