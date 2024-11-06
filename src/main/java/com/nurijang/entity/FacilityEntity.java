package com.nurijang.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "facility")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class FacilityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String ctprvnCd; // 시도코드
    private String ctprvnNm; // 시도명
    private String signguCd; // 시군구코드
    private String signguNm; // 시군구명
    private String fcltyNm; // 시설명
    private String rprsntvTelNo; // 대표자전화번호
    private String zipNo; // 우편번호
    private String fcltyAddr; // 시설주소
    private String fcltyDetailAddr; // 시설상세주소
    private String mainItemCd; // 주요종목코드
    private String mainItemNm; // 주요종목명
    private Double fcltyCrdntLo; // 시설좌표경도
    private Double fcltyCrdntLa; // 시설좌표위도
    private String courseFlagCd; // 강좌구분코드

}
