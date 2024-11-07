package com.nurijang.service;

import com.nurijang.dto.GetFacilitiesResponse;
import com.nurijang.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public List<GetFacilitiesResponse> getFacilities(double latitude, double longitude) {
        double latitudeOffset = 1.5 / 111.0;
        double longitudeOffset = 1.5 / (111.0 * Math.cos(Math.toRadians(latitude)));

        double minLatitude = latitude - latitudeOffset;
        double maxLatitude = latitude + latitudeOffset;
        double minLongitude = longitude - longitudeOffset;
        double maxLongitude = longitude + longitudeOffset;

        return facilityRepository.findFacilitiesWithinSquare(minLatitude, maxLatitude, minLongitude, maxLongitude)
                .stream()
                .map(facilityEntity -> new GetFacilitiesResponse(
                        facilityEntity.getId(),
                        facilityEntity.getFcltyNm(),
                        facilityEntity.getFcltyAddr(),
                        facilityEntity.getFcltyDetailAddr(),
                        facilityEntity.getMainItemNm(),
                        facilityEntity.getFcltyCrdntLo(),
                        facilityEntity.getFcltyCrdntLa()))
                .collect(Collectors.toList());
    }
}
