package com.nurijang.repository;

import com.nurijang.entity.FacilityDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface FacilityDocumentRepository extends ElasticsearchRepository<FacilityDocument, String> {
    List<FacilityDocument> findByMainItemNmContainingOrFcltyNmContaining(String mainItemNm, String fcltyNm);
    List<FacilityDocument> findByMainItemNmContaining(String mainItemNm);
    List<FacilityDocument> findByFcltyNmContaining(String fcltyNm);
}
