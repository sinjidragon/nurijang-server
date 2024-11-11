package com.nurijang.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SearchFacilitiesRequest {

    @NotNull
    private Double fcltyCrdntLo;
    @NotNull
    private Double fcltyCrdntLa;

    @NotNull
    private String searchText;
}
