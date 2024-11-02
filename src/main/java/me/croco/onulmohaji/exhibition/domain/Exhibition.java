package me.croco.onulmohaji.exhibition.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
public class Exhibition {

    @Id
    @Column
    private Long seq;   //	일련번호	9	1	12402

    @Column
    private String title;   //	제목	200	1	칼라바 쇼

    @Column(name = "start_date")
    private String startDate;   //	시작일	8	1	20100101

    @Column(name = "end_date")
    private String endDate; //	마감일	8	1	20100107

    @Column(name = "place")
    private String place;    //	장소	50	1	폴리미디어 씨어터

    @Column(name = "realm_name")
    private String realmName;    //	분류명	10	1	연극

    @Column(name = "area")
    private String area;    //	지역	10	0	서울

    @Column(name = "thumbnail")
    private String thumbnail;   //	썸네일	256	1

    @Column(name = "gps_x")
    private Double gpsX; //	GPS-X좌표	11	0	129.1013129	경도

    @Column(name = "gps_y")
    private Double gpsY;    //	GPS-Y좌표	11	0	35.1416412	위도

    @Column(name = "wpointx")
    private Long wpointx;

    @Column(name = "wpointy")
    private Long wpointy;

}
