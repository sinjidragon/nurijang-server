package com.nurijang.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetFacilitiesResponse {
    private int id;
    private String fcltyNm; // 시설명
    private String fcltyAddr; // 시설주소
    private String fcltyDetailAddr; // 시설상세주소
    private String mainItemNm; // 주요종목명
    private Double fcltyCrdntLo; // 시설좌표경도
    private Double fcltyCrdntLa; // 시설좌표위도

    public GetFacilitiesResponse(int id, String fcltyNm, String fcltyAddr, String fcltyDetailAddr, String mainItemNm, Double fcltyCrdntLo, Double fcltyCrdntLa) {
        this.id = id;
        this.fcltyNm = fcltyNm;
        this.fcltyAddr = fcltyAddr;
        this.fcltyDetailAddr = fcltyDetailAddr;
        this.mainItemNm = mainItemNm;
        this.fcltyCrdntLo = fcltyCrdntLo;
        this.fcltyCrdntLa = fcltyCrdntLa;
    }
}
