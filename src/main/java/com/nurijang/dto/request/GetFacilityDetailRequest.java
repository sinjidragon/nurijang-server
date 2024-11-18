package com.nurijang.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GetFacilityDetailRequest {

    private int id;

    @NotNull
    private Double fcltyCrdntLo;

    @NotNull
    private Double fcltyCrdntLa;

}
