package com.nurijang.service;

import com.nurijang.dto.response.GetFacilitiesResponse;
import com.nurijang.dto.request.SearchFacilitiesRequest;
import com.nurijang.dto.response.SearchFacilitiesResponse;
import com.nurijang.dto.response.SuggestionFacilityResponse;
import com.nurijang.entity.FacilityDocument;
import com.nurijang.entity.FacilityEntity;
import com.nurijang.repository.FacilityDocumentRepository;
import com.nurijang.repository.FacilityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityDocumentRepository facilityDocumentRepository;

    @Transactional
    public void indexData() {
        List<FacilityEntity> entities = facilityRepository.findAll();

        for (FacilityEntity entity : entities) {
            FacilityDocument document = FacilityDocument.builder()
                    .id(String.valueOf(entity.getId()))
                    .fcltyNm(entity.getFcltyNm())
                    .fcltyAddr(entity.getFcltyAddr())
                    .fcltyDetailAddr(entity.getFcltyDetailAddr())
                    .rprsntvTelNo(entity.getRprsntvTelNo())
                    .mainItemNm(entity.getMainItemNm())
                    .fcltyCrdntLo(entity.getFcltyCrdntLo())
                    .fcltyCrdntLa(entity.getFcltyCrdntLa())
                    .build();

            facilityDocumentRepository.save(document);
        }
        log.info("Facilities indexed");
    }

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

    public List<GetFacilitiesResponse> searchFacilities(SearchFacilitiesRequest request) {
        Point2D location2 = new Point2D.Double(request.getFcltyCrdntLa(), request.getFcltyCrdntLo());
        return facilityDocumentRepository.findByMainItemNmContainingOrFcltyNmContaining(request.getSearchText(), request.getSearchText())
                .stream()
                .map(facilityDocument ->{
                    Point2D.Double facilityLocation = new Point2D.Double(facilityDocument.getFcltyCrdntLa(), facilityDocument.getFcltyCrdntLo());

                    double distance = location2.distance(facilityLocation) * 111.0;

                    return new GetFacilitiesResponse(
                            Integer.parseInt(facilityDocument.getId()),
                            distance,
                            facilityDocument.getFcltyNm(),
                            facilityDocument.getFcltyAddr(),
                            facilityDocument.getFcltyDetailAddr(),
                            facilityDocument.getRprsntvTelNo(),
                            facilityDocument.getMainItemNm(),
                            facilityDocument.getFcltyCrdntLo(),
                            facilityDocument.getFcltyCrdntLa()
                    );
                })
                .sorted(Comparator.comparingDouble(GetFacilitiesResponse::getDistance))
                .collect(Collectors.toList());
    }

    public SearchFacilitiesResponse getAutocompleteSuggestions(SearchFacilitiesRequest request) {
        Point2D location2 = new Point2D.Double(request.getFcltyCrdntLa(), request.getFcltyCrdntLo());

        List<FacilityDocument> mainItemResults = facilityDocumentRepository
                .findByMainItemNmContaining(request.getSearchText())
                .stream()
                .sorted(Comparator.comparingDouble(f -> calculateDistance(f, location2)))
                .limit(5)
                .collect(Collectors.toList());

        Set<String> recommendedMainItemsSet = mainItemResults.stream()
                .map(FacilityDocument::getMainItemNm)
                .collect(Collectors.toSet());

        List<String> recommendedMainItems = new ArrayList<>(recommendedMainItemsSet);

        List<FacilityDocument> facilityResults = facilityDocumentRepository
                .findByFcltyNmContaining(request.getSearchText())
                .stream()
                .sorted(Comparator.comparingDouble(f -> calculateDistance(f, location2)))
                .limit(5)
                .collect(Collectors.toList());

        List<SuggestionFacilityResponse> recommendedFacilities = facilityResults.stream()
                .map(facilityDocument -> new SuggestionFacilityResponse(
                        Integer.parseInt(facilityDocument.getId()),
                        calculateDistance(facilityDocument, location2),
                        facilityDocument.getFcltyNm()
                ))
                .sorted(Comparator.comparingDouble(SuggestionFacilityResponse::getDistance))
                .collect(Collectors.toList());

        return new SearchFacilitiesResponse(recommendedMainItems, recommendedFacilities);
    }
    private double calculateDistance(FacilityDocument facilityDocument, Point2D userLocation) {
        Point2D facilityLocation = new Point2D.Double(facilityDocument.getFcltyCrdntLa(), facilityDocument.getFcltyCrdntLo());
        return userLocation.distance(facilityLocation) * 111.0; // 거리 계산 (단위: km)
    }
}
