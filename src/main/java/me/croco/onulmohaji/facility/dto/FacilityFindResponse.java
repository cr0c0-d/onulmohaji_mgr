package me.croco.onulmohaji.facility.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.facility.domain.Facility;
import me.croco.onulmohaji.util.Haversine;

@Setter
@Getter
public class FacilityFindResponse {
    private Long id; //  "16618597",
    private String placeName; //  "장생당약국",
    private String placeTypeName;
    private Double distance; //  미터 기준 거리
    private String placeUrl; // http://place.map.kakao.com/16618597",
    private String categoryName; //  "의료,건강 > 약국",
    private String addressName; //  "서울 강남구 대치동 943-16",
    private String roadAddressName; //  "서울 강남구 테헤란로84길 17",
    private String phone; //  "02-558-5476",
    private String categoryGroupCode; //  "PM9",
    private String categoryGroupName; //  "약국",
    private Double longitude; //  "127.05897078335246",
    private Double latitude; //  "37.506051888130386"
    private String thumbnail;
    private String tags;
    private Integer scoresum;
    private Integer scorecnt;
    private Long wpointx;
    private Long wpointy;

    public FacilityFindResponse(Facility facility, Double latitude, Double longitude) {
        this.id = facility.getId();
        this.placeName = facility.getPlaceName();
        this.placeTypeName = facility.getCategoryName().substring(facility.getCategoryName().lastIndexOf(" > ") + 3);
        this.distance = Haversine.getDistance(latitude, longitude, facility);
        this.placeUrl = facility.getPlaceUrl();
        this.categoryName = facility.getCategoryName();
        this.addressName = facility.getAddressName();
        this.roadAddressName = facility.getRoadAddressName();
        this.phone = facility.getPhone();
        this.categoryGroupCode = facility.getCategoryGroupCode();
        this.categoryGroupName = facility.getCategoryGroupName();
        this.longitude = facility.getLongitude();
        this.latitude = facility.getLatitude();
        this.thumbnail = facility.getThumbnail();
        this.tags = facility.getTags();
        this.scoresum = facility.getScoresum();
        this.scorecnt = facility.getScorecnt();
        this.wpointx = facility.getWpointx();
        this.wpointy = facility.getWpointy();
    }
}
