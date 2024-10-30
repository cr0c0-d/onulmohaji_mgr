package me.croco.onulmohaji.exhibition.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.exhibition.domain.Exhibition;

@Getter
@Setter
public class ExhibitionListFindResponse {

    private Long seq;   //	일련번호	9	1	12402
    private String title;   //	제목	200	1	칼라바 쇼
    private String startDate;   //	시작일	8	1	20100101
    private String endDate; //	마감일	8	1	20100107
    private String place;    //	장소	50	1	폴리미디어 씨어터
    private String realmName;    //	분류명	10	1	연극
    private String area;    //	지역	10	0	서울
    private String thumbnail;   //	썸네일	256	1
    private Double gpsX; //	GPS-X좌표	11	0	129.1013129	경도
    private Double gpsY;    //	GPS-Y좌표	11	0	35.1416412	위도

    public ExhibitionListFindResponse(Exhibition exhibition) {
        this.seq = exhibition.getSeq();
        this.title = exhibition.getTitle();
        this.startDate = exhibition.getStartDate();
        this.endDate = exhibition.getEndDate();
        this.place = exhibition.getPlace();
        this.realmName = exhibition.getRealmName();
        this.area = exhibition.getArea();
        this.thumbnail = exhibition.getThumbnail();
        this.gpsX = exhibition.getGpsX();
        this.gpsY = exhibition.getGpsY();
    }
}
