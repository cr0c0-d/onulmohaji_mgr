package me.croco.onulmohaji.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KakaoLocalListFindResponse {

    private String place_name; //  "장생당약국",
    private int distance; //  미터 기준 거리
    private String place_url; // http://place.map.kakao.com/16618597",
    private String category_name; //  "의료,건강 > 약국",
    private String address_name; //  "서울 강남구 대치동 943-16",
    private String road_address_name; //  "서울 강남구 테헤란로84길 17",
    private Long id; //  "16618597",
    private String phone; //  "02-558-5476",
    private String category_group_code; //  "PM9",
    private String category_group_name; //  "약국",
    private Double x; //  "127.05897078335246",
    private Double y; //  "37.506051888130386"

}
