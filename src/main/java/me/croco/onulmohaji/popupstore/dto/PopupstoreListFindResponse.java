package me.croco.onulmohaji.popupstore.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.popupstore.domain.Popupstore;

@Setter
@Getter
public class PopupstoreListFindResponse {
    private Long storeId;
    private String name;
    private int categoryId;
    private String mainBrand;
    private String brand_1;
    private String brand_2;
    private String title;
    private String topLevelAddress;
    private String address;
    private boolean preRegister;
    private String preRegisterStartDate;
    private String preRegisterEndDate;
    private String preRegisterLink;
    private String hashtag;
    private String thumbnails;
    private Double latitude;
    private Double longitude;
    private String searchItems;
    private String startDate;
    private String endDate;
    private String workingTime;
    private String status;

    public PopupstoreListFindResponse(Popupstore popupstore) {
        this.storeId = popupstore.getStoreId();
        this.name = popupstore.getName();
        this.categoryId = popupstore.getCategoryId();
        this.mainBrand = popupstore.getMainBrand();
        this.brand_1 = popupstore.getBrand_1();
        this.brand_2 = popupstore.getBrand_2();
        this.title = popupstore.getTitle();
        this.topLevelAddress = popupstore.getTopLevelAddress();
        this.address = popupstore.getAddress();
        this.preRegister = popupstore.isPreRegister();
        this.preRegisterStartDate = popupstore.getPreRegisterStartDate();
        this.preRegisterEndDate = popupstore.getPreRegisterEndDate();
        this.preRegisterLink = popupstore.getPreRegisterLink();
        this.hashtag = popupstore.getHashtag();
        this.thumbnails = popupstore.getThumbnails();
        this.latitude = popupstore.getLatitude();
        this.longitude = popupstore.getLongitude();
        this.searchItems = popupstore.getSearchItems();
        this.startDate = popupstore.getStartDate();
        this.endDate = popupstore.getEndDate();
        this.workingTime = popupstore.getWorkingTime();
        this.status = popupstore.getStatus();
    }
}
