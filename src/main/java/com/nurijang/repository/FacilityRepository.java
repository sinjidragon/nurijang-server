package com.nurijang.repository;

import com.nurijang.entity.FacilityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<FacilityEntity, Long> {
    FacilityEntity findById(int id);
}
