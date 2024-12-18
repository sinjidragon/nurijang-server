package com.nurijang.entity;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@Getter
@Document(indexName = "facility")
public class FacilityDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "edge_ngram_analyzer")
    private String fcltyNm; // 시설명

    private String fcltyAddr; // 시설주소

    private String fcltyDetailAddr; // 시설상세주소

    private String rprsntvTelNo; // 대표자 연락처

    @Field(type = FieldType.Text, analyzer = "edge_ngram_analyzer")
    private String mainItemNm; // 주요종목명

    private Double fcltyCrdntLo; // 시설좌표경도
    private Double fcltyCrdntLa; // 시설좌표위도
}
