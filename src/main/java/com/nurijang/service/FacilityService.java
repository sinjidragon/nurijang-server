package com.nurijang.service;

import com.nurijang.dto.GetFacilitiesRequest;
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

    public List<GetFacilitiesResponse> getFacilities(GetFacilitiesRequest request) {
        double latitudeOffset = 3 / 111.0;
        double longitudeOffset = 3 / (111.0 * Math.cos(Math.toRadians(request.getFcltyCrdntLo())));

        double minLatitude = request.getFcltyCrdntLa() - latitudeOffset;
        double maxLatitude = request.getFcltyCrdntLa() + latitudeOffset;
        double minLongitude = request.getFcltyCrdntLo() - longitudeOffset;
        double maxLongitude = request.getFcltyCrdntLo() + longitudeOffset;

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
