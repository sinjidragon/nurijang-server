package com.nurijang.service;

import com.nurijang.dto.request.SearchFacilitiesRequest;
import com.nurijang.dto.response.GetFacilitiesResponse;
import com.nurijang.dto.response.SearchFacilitiesResponse;
import com.nurijang.dto.response.SuggestionFacilityResponse;
import com.nurijang.entity.FacilityDocument;
import com.nurijang.repository.FacilityDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
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
public class SearchService {

    private final FacilityDocumentRepository facilityDocumentRepository;

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

    private static final double EARTH_RADIUS = 6371.0;

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    public List<GetFacilitiesResponse> searchFacilitiesByItem(SearchFacilitiesRequest request) {
        double userLat = request.getFcltyCrdntLa();
        double userLon = request.getFcltyCrdntLo();

        return facilityDocumentRepository.findByMainItemNm(request.getSearchText())
                .stream()
                .map(facilityDocument -> {
                    double facilityLat = facilityDocument.getFcltyCrdntLa();
                    double facilityLon = facilityDocument.getFcltyCrdntLo();

                    double distance = calculateDistance(userLat, userLon, facilityLat, facilityLon);

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

    @Cacheable(value = "autocompleteSuggestions", key = "#request.searchText + #request.fcltyCrdntLa + #request.fcltyCrdntLo")
    public SearchFacilitiesResponse getAutocompleteSuggestions(SearchFacilitiesRequest request) {
        Point2D location2 = new Point2D.Double(request.getFcltyCrdntLa(), request.getFcltyCrdntLo());

        List<FacilityDocument> results = facilityDocumentRepository
                .findByMainItemNmStartingWithOrFcltyNmStartingWith(request.getSearchText(), request.getSearchText());

        Set<String> recommendedMainItemsSet = results.stream()
                .filter(doc -> doc.getMainItemNm().contains(request.getSearchText()))
                .sorted(Comparator.comparingDouble(f -> calculateDistance(f, location2)))
                .map(FacilityDocument::getMainItemNm)
                .collect(Collectors.toSet());

        List<String> recommendedMainItems = new ArrayList<>(recommendedMainItemsSet);

        List<SuggestionFacilityResponse> recommendedFacilities = results.stream()
                .filter(doc -> doc.getFcltyNm().startsWith(request.getSearchText()))
                .sorted(Comparator.comparingDouble(f -> calculateDistance(f, location2)))
                .limit(5)
                .map(facilityDocument -> new SuggestionFacilityResponse(
                        Integer.parseInt(facilityDocument.getId()),
                        calculateDistance(facilityDocument, location2),
                        facilityDocument.getFcltyNm()
                ))
                .collect(Collectors.toList());

        return new SearchFacilitiesResponse(recommendedMainItems, recommendedFacilities);
    }

    private double calculateDistance(FacilityDocument facilityDocument, Point2D userLocation) {
        Point2D facilityLocation = new Point2D.Double(facilityDocument.getFcltyCrdntLa(), facilityDocument.getFcltyCrdntLo());
        return userLocation.distance(facilityLocation) * 111.0;
    }
}
