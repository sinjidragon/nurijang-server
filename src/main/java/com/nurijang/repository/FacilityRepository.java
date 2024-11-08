package com.nurijang.repository;

import com.nurijang.entity.FacilityEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FacilityRepository extends JpaRepository<FacilityEntity, Long> {
    FacilityEntity findById(int id);

    @Query("SELECT f FROM FacilityEntity f " +
            "WHERE f.fcltyCrdntLa BETWEEN :minLatitude AND :maxLatitude " +
            "AND f.fcltyCrdntLo BETWEEN :minLongitude AND :maxLongitude")
    List<FacilityEntity> findFacilitiesWithinSquare(
            @Param("minLatitude") double minLatitude,
            @Param("maxLatitude") double maxLatitude,
            @Param("minLongitude") double minLongitude,
            @Param("maxLongitude") double maxLongitude);

    @Query("SELECT f FROM FacilityEntity f WHERE f.fcltyNm LIKE %:searchText% OR f.mainItemNm LIKE %:searchText%")
    List<FacilityEntity> searchFacilities(String searchText);
}
