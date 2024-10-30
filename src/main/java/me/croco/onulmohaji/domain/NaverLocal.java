package me.croco.onulmohaji.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * 네이버 지역 검색 API의 결과값을 담는 객체
 */
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverLocal {

    private String title;   // 업체, 기관의 이름
    private String link;		// 업체, 기관의 상세 정보 URL
    private String category;		// 업체, 기관의 분류 정보
    private String description;		// 업체, 기관에 대한 설명
    private String telephone;		// 값을 반환하지 않는 요소. 하위 호환성을 유지하기 위해 있는 요소입니다.
    private String address;		// 업체, 기관명의 지번 주소
    private String roadAddress;		// 업체, 기관명의 도로명 주소
    private int mapx;		// 업체, 기관이 위치한 장소의 x 좌표(KATECH 좌표계 기준). 네이버 지도 API에서 사용할 수 있습니다.
    private int mapy;		// 업체, 기관이 위치한 장소의 y 좌표(KATECH 좌표계 기준). 네이버 지도 API에서 사용할 수 있습니다.
}
