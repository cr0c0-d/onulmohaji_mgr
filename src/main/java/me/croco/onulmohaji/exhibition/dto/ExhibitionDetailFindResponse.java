package me.croco.onulmohaji.exhibition.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.exhibition.domain.Exhibition;
import me.croco.onulmohaji.exhibition.domain.ExhibitionDetail;
import me.croco.onulmohaji.festival.domain.Festival;

@Setter
@Getter
public class ExhibitionDetailFindResponse {

    private String placeId;
    private String placeName;
    private String placeType = "festival";
    private String placeTypeName = "축제";
    private String startDate;
    private String endDate;
    private Double latitude;
    private Double longitude;
    private String thumbnail;
    private String address;
    private String url;
    private String placeUrl;

    private String contents1;
    private String contents2;
    private String contact;
    private String fare;


    public ExhibitionDetailFindResponse(Exhibition exhibition, ExhibitionDetail exhibitionDetail) {
        this.placeId = String.valueOf(exhibition.getSeq());
        this.placeName = exhibition.getTitle();
        this.startDate = exhibition.getStartDate();
        this.endDate = exhibition.getEndDate();
        this.latitude = exhibition.getGpsY();
        this.longitude = exhibition.getGpsX();
        this.thumbnail = exhibition.getThumbnail();
        this.address = exhibitionDetail.getPlaceAddr();
        this.url = exhibitionDetail.getUrl();
        this.placeUrl = exhibitionDetail.getPlaceUrl();

        this.contents1 = exhibitionDetail.getContents1();
        this.contents2 = exhibitionDetail.getContents2();
        this.contact = exhibitionDetail.getPhone();
        this.fare = exhibitionDetail.getPrice();

    }
}
