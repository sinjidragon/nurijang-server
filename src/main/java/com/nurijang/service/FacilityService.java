package com.nurijang.service;

import com.nurijang.dto.GetFacilitiesResponse;
import com.nurijang.dto.SearchFacilitiesRequest;
import com.nurijang.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public List<GetFacilitiesResponse> getFacilities(double latitude, double longitude) {
        Point2D location1 = new Point2D.Double(latitude, longitude);

        double latitudeOffset = 3 / 111.0;
        double longitudeOffset = 3 / (111.0 * Math.cos(Math.toRadians(latitude)));

        double minLatitude = latitude - latitudeOffset;
        double maxLatitude = latitude + latitudeOffset;
        double minLongitude = longitude - longitudeOffset;
        double maxLongitude = longitude + longitudeOffset;

        return facilityRepository.findFacilitiesWithinSquare(minLatitude, maxLatitude, minLongitude, maxLongitude)
                .stream()
                .map(facility -> {
                    Point2D.Double facilityLocation = new Point2D.Double(facility.getFcltyCrdntLa(), facility.getFcltyCrdntLo());

                    double distance = location1.distance(facilityLocation) * 111.0;

                    return new GetFacilitiesResponse(
                            facility.getId(),
                            distance,
                            facility.getFcltyNm(),
                            facility.getFcltyAddr(),
                            facility.getFcltyDetailAddr(),
                            facility.getRprsntvTelNo(),
                            facility.getMainItemNm(),
                            facility.getFcltyCrdntLo(),
                            facility.getFcltyCrdntLa()
                    );
                })
                .sorted(Comparator.comparingDouble(GetFacilitiesResponse::getDistance))
                .collect(Collectors.toList());
    }

    public List<GetFacilitiesResponse> searchFacilities(SearchFacilitiesRequest searchFacilitiesRequest) {
        Point2D location2 = new Point2D.Double(searchFacilitiesRequest.getFcltyCrdntLa(), searchFacilitiesRequest.getFcltyCrdntLo());
        return facilityRepository.searchFacilities(searchFacilitiesRequest.getSearchText())
                .stream()
                .map(facility ->{
                    Point2D.Double facilityLocation = new Point2D.Double(facility.getFcltyCrdntLa(), facility.getFcltyCrdntLo());

                    double distance = location2.distance(facilityLocation) * 111.0;

                    return new GetFacilitiesResponse(
                            facility.getId(),
                            distance,
                            facility.getFcltyNm(),
                            facility.getFcltyAddr(),
                            facility.getFcltyDetailAddr(),
                            facility.getRprsntvTelNo(),
                            facility.getMainItemNm(),
                            facility.getFcltyCrdntLo(),
                            facility.getFcltyCrdntLa()
                    );
                })
                .sorted(Comparator.comparingDouble(GetFacilitiesResponse::getDistance))
                .collect(Collectors.toList());
    }
}
