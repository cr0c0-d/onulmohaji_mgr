package me.croco.onulmohaji.facility.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import me.croco.onulmohaji.api.dto.KakaoLocalListFindResponse;

@Data
public class Facility {

    @Id
    @Column
    private Long id; //  "16618597",

    @Column(name = "place_name")
    private String placeName; //  "장생당약국",

    @Column(name = "distance")
    private int distance; //  미터 기준 거리

    @Column(name = "place_url")
    private String placeUrl; // http://place.map.kakao.com/16618597",

    @Column(name = "category_name")
    private String categoryName; //  "의료,건강 > 약국",

    @Column(name = "address_name")
    private String addressName; //  "서울 강남구 대치동 943-16",

    @Column(name = "road_address_name")
    private String roadAddressName; //  "서울 강남구 테헤란로84길 17",

    @Column(name = "phone")
    private String phone; //  "02-558-5476",

    @Column(name = "category_group_code")
    private String categoryGroupCode; //  "PM9",

    @Column(name = "category_group_name")
    private String categoryGroupName; //  "약국",

    @Column(name = "longitude")
    private Double longitude; //  "127.05897078335246",

    @Column(name = "latitude")
    private Double latitude; //  "37.506051888130386"

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "tags")
    private String tags;

    @Column(name = "scoresum")
    private Integer scoresum;

    @Column(name = "scorecnt")
    private Integer scorecnt;

    @Column(name = "wpointx")
    private Long wpointx;

    @Column(name = "wpointy")
    private Long wpointy;


    public Facility(KakaoLocalListFindResponse response) {
        this.id = response.getId();
        this.placeName = response.getPlace_name();
        this.distance = response.getDistance();
        this.placeUrl = response.getPlace_url();
        this.categoryName = response.getCategory_name();
        this.addressName = response.getAddress_name();
        this.roadAddressName = response.getRoad_address_name();
        this.phone = response.getPhone();
        this.categoryGroupCode = response.getCategory_group_code();
        this.categoryGroupName = response.getCategory_group_name();
        this.longitude = response.getX();
        this.latitude = response.getY();
    }
}
