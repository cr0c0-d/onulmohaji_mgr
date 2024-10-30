package me.croco.onulmohaji.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.exhibition.domain.Exhibition;
import me.croco.onulmohaji.exhibition.domain.ExhibitionDetail;
import me.croco.onulmohaji.festival.domain.Festival;
import me.croco.onulmohaji.popupstore.domain.Popupstore;
import me.croco.onulmohaji.popupstore.domain.PopupstoreDetail;
import me.croco.onulmohaji.popupstore.domain.PopupstoreImage;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PlaceDetailFindResponse {
    private String id;
    private String title;
    private String subTitle;
    private String startDate;
    private String endDate;
    private String contents1;
    private String contents2;
    private String url;
    private Double gpsX;
    private Double gpsY;
    private List<String> imageList;
    private String address;


    public PlaceDetailFindResponse(Popupstore popupstore, PopupstoreDetail detail, List<PopupstoreImage> imageList) {
        this.id = String.valueOf(popupstore.getStoreId());
        this.title = HtmlUtils.htmlUnescape(popupstore.getTitle());
        this.startDate = popupstore.getStartDate();
        this.endDate = popupstore.getEndDate();
        this.contents1 = detail.getNotice();
        this.contents2 = detail.getContents();
        this.url = detail.getBrandUrl();
        this.gpsX = popupstore.getLongitude();
        this.gpsY = popupstore.getLatitude();
        this.imageList = imageList.stream().map(PopupstoreImage::getUrl).toList();
        this.address = popupstore.getAddress();
    }

    public PlaceDetailFindResponse(Exhibition exhibition, ExhibitionDetail detail) {
        this.id = String.valueOf(exhibition.getSeq());
        this.title = HtmlUtils.htmlUnescape(exhibition.getTitle());
        this.startDate = exhibition.getStartDate();
        this.endDate = exhibition.getEndDate();
        this.contents1 = detail.getContents1();
        this.contents2 = detail.getContents2();
        this.url = detail.getUrl();
        this.gpsX = detail.getGpsX();
        this.gpsY = detail.getGpsY();
        this.imageList = new ArrayList<>();
        this.imageList.add(detail.getImgUrl());
        this.address = detail.getPlaceAddr();
    }

    public PlaceDetailFindResponse(Festival festival) {
        this.id = festival.getId();
        this.title = festival.getTitle();
        this.startDate = festival.getStartDate();
        this.endDate = festival.getEndDate();
        this.contents1 = festival.getContents1();
        this.contents2 = festival.getContents2();
        this.url = festival.getHomepageUrl();
        this.gpsX = festival.getLongitude();
        this.gpsY = festival.getLatitude();
        this.imageList = new ArrayList<>();
        this.imageList.add(festival.getThumbnail());
        this.address = festival.getAddress();
    }

}
