package me.croco.onulmohaji.exhibition.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
public class ExhibitionDetail {

    @Id
    @Column
    private Long seq; //일련번호 - 예 : 12402

    @Column
    private String title; //제목 - 예 : 칼라바 쇼

    @Column(name = "start_date")
    private String startDate; //시작일 - 예 : 20100101

    @Column(name = "end_date")
    private String endDate; //마감일 - 예 : 20100107

    @Column(name = "place")
    private String place; //장소 - 예 : 폴리미디어 씨어터

    @Column(name = "realm_name")
    private String realmName; //분류명 - 예 : 연극

    @Column(name = "area")
    private String area; //지역 - 예 : 서울

    @Column(name = "sub_title")
    private String subTitle; //공연부제목 - 예 : 장사익 콘서트

    @Column(name = "price")
    private String price; //티켓요금 - 예 : A석 10,000원 B석 8,000원

    @Column(name = "contents1")
    private String contents1; //내용1

    @Column(name = "contents2")
    private String contents2; //내용2

    @Column(name = "url")
    private String url; //관람URL

    @Column(name = "phone")
    private String phone; //문의처

    @Column(name = "gps_x")
    private Double gpsX; //GPS-X좌표 - 예 : 129.1013129

    @Column(name = "gps_y")
    private Double gpsY; //GPS-Y좌표 - 예 : 35.1416412

    @Column(name = "img_url")
    private String imgUrl; //이미지

    @Column(name = "place_url")
    private String placeUrl; //공연장URL

    @Column(name = "place_addr")
    private String placeAddr; //공연장 주소

    @Column(name = "place_seq")
    private String placeSeq; //문화예술공간 일련번호 - 예 : 12345
}
