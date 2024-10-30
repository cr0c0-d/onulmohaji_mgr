package me.croco.onulmohaji.facility.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.KakaoLocalService;
import me.croco.onulmohaji.facility.domain.Facility;
import me.croco.onulmohaji.facility.dto.FacilityFindResponse;
import me.croco.onulmohaji.facility.dto.FacilityListFindResponse;
import me.croco.onulmohaji.facility.service.FacilityService;
import me.croco.onulmohaji.localcode.domain.Localcode;
import me.croco.onulmohaji.localcode.service.LocalcodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;
    private final LocalcodeService localcodeService;


    @GetMapping("/api/facility")
    public ResponseEntity<List<FacilityFindResponse>> findFacility(@RequestParam String categoryId, @RequestParam Double latitude, @RequestParam Double longitude, @RequestParam(required = false) String keyword) {
        List<Facility> facilityList = facilityService.getNewFacilities(keyword, categoryId, latitude, longitude);
        return ResponseEntity.ok()
                .body(facilityList.stream().map(facility -> new FacilityFindResponse(facility, latitude, longitude)).toList());
    }

    @GetMapping("/api/facility/local/list")
    public ResponseEntity<List<FacilityListFindResponse>> findFacilityListByLocalcode(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam int distance, @RequestParam(required = false) String keyword) {

        List<FacilityListFindResponse> facilityList = facilityService.findLocalFacilityList(keyword, latitude, longitude, distance);

        return ResponseEntity.ok()
                .body(facilityList);
    }

    @GetMapping("/api/facility/place/list")
    public ResponseEntity<List<FacilityFindResponse>> findFacilityListByPlace(@RequestParam Double latitude, @RequestParam Double longitude, @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok()
                .body(facilityService.findFoodListByPlace(latitude, longitude).stream().map(facility -> new FacilityFindResponse(facility, latitude, longitude)).toList());
    }
}
