package me.croco.onulmohaji.popupstore.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.popupstore.domain.Popupstore;
import me.croco.onulmohaji.popupstore.domain.PopupstoreDetail;
import me.croco.onulmohaji.popupstore.domain.PopupstoreImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class PopupDetailFindResponse {

    private String placeId;
    private String placeName;
    private String placeType = "popup";
    private String placeTypeName = "팝업스토어";
    private String startDate;
    private String endDate;
    private Double latitude;
    private Double longitude;
    private String thumbnail;
    private List<String> imageList;
    private String address;
    private Map<String, String> preRegisterInfo;
    private String workingTime;


    private String contents1;
    private String contents2;
    private String brandUrl;
    private String instaUrl;
    private int parking;
    private int free;
    private int noKids;
    private int food;
    private int pet;
    private int adult;
    private int wifi;


    public PopupDetailFindResponse(Popupstore popupstore, PopupstoreDetail popupstoreDetail, List<PopupstoreImage> images) {
        this.placeId = String.valueOf(popupstore.getStoreId());
        this.placeName = popupstore.getName();

        this.startDate = popupstore.getStartDate();
        this.endDate = popupstore.getEndDate();
        this.latitude = popupstore.getLatitude();
        this.longitude = popupstore.getLongitude();
        this.thumbnail = popupstore.getThumbnails();
        this.imageList = images.stream().map(PopupstoreImage::getUrl).toList();
        this.address = popupstore.getAddress();

        if(popupstore.isPreRegister()) {
            Map<String, String> preRegisterInfo = new HashMap<>();
            preRegisterInfo.put("startDate", popupstore.getPreRegisterStartDate());
            preRegisterInfo.put("endDate", popupstore.getPreRegisterEndDate());
            preRegisterInfo.put("link", popupstore.getPreRegisterLink());
            this.preRegisterInfo = preRegisterInfo;
        }

        this.workingTime = popupstore.getWorkingTime().replace("_date", "Date");

        this.contents1 = popupstoreDetail.getNotice();
        this.contents2 = popupstoreDetail.getContents();
        this.brandUrl = popupstoreDetail.getBrandUrl();
        this.instaUrl = popupstoreDetail.getInstaUrl();
        this.parking = popupstoreDetail.isParking() ? 1 : 0;
        this.free = popupstoreDetail.isFree() ? 1 : 0;
        this.noKids = popupstoreDetail.isNoKids() ? 1 : 0;
        this.food = popupstoreDetail.isFood() ? 1 : 0;
        this.pet = popupstoreDetail.isPet() ? 1 : 0;
        this.adult = popupstoreDetail.isAdult() ? 1 : 0;
        this.wifi = popupstoreDetail.isWifi() ? 1 : 0;
    }
}
