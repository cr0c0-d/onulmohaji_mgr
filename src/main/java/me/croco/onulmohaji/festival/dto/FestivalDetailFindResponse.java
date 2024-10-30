package me.croco.onulmohaji.festival.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.festival.domain.Festival;
import me.croco.onulmohaji.popupstore.domain.Popupstore;
import me.croco.onulmohaji.popupstore.domain.PopupstoreDetail;
import me.croco.onulmohaji.popupstore.domain.PopupstoreImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class FestivalDetailFindResponse {

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

    private String contents1;
    private String contents2;
    private String homepageUrl;
    private String instaUrl;
    private String contact;
    private String fare;


    public FestivalDetailFindResponse(Festival festival) {
        this.placeId = festival.getId();
        this.placeName = festival.getTitle();
        this.startDate = festival.getStartDate();
        this.endDate = festival.getEndDate();
        this.latitude = festival.getLatitude();
        this.longitude = festival.getLongitude();
        this.thumbnail = festival.getThumbnail();
        this.address = festival.getAddress();

        this.contents1 = festival.getContents1();
        this.contents2 = festival.getContents2();
        this.homepageUrl = festival.getHomepageUrl();
        this.instaUrl = festival.getInstaUrl();
        this.contact = festival.getContact();
        this.fare = festival.getFare();

    }
}
