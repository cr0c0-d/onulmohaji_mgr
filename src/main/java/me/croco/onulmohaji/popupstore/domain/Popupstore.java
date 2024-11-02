package me.croco.onulmohaji.popupstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import me.croco.onulmohaji.api.dto.PopplyPopupstoreFindResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Popupstore {

    @Id
    @Column(name = "store_id")
    private Long storeId;

    @Column
    private String name;

    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "main_brand")
    private String mainBrand;

    @Column
    private String brand_1;

    @Column
    private String brand_2;

    @Column
    private String title;

    @Column(name = "top_level_address")
    private String topLevelAddress;

    @Column
    private String address;

    @Column(name = "pre_register")
    private boolean preRegister;

    @Column(name = "pre_register_start_date")
    private String preRegisterStartDate;

    @Column(name = "pre_register_end_date")
    private String preRegisterEndDate;

    @Column(name = "pre_register_link")
    private String preRegisterLink;

    @Column
    private String hashtag;

    @Column
    private String thumbnails;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(name = "search_items")
    private String searchItems;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "working_time")
    private String workingTime;

    @Column
    private String status;

    @Column(name = "wpointx")
    private Long wpointx;

    @Column(name = "wpointy")
    private Long wpointy;

    @Builder
    public Popupstore(PopplyPopupstoreFindResponse response) {
        this.storeId = response.getStoreId();
        this.name = response.getName();
        this.categoryId = response.getCategoryId();
        this.mainBrand = response.getMainBrand();
        this.brand_1 = response.getBrand_1();
        this.brand_2 = response.getBrand_2();
        this.title = response.getTitle();
        this.topLevelAddress = response.getTopLevelAddress();
        this.address = response.getAddress();
        this.preRegister = response.isPreRegister();
        this.preRegisterStartDate = response.getPreRegisterStartDate().substring(0, 10);
        this.preRegisterEndDate = response.getPreRegisterEndDate().substring(0, 10);
        this.preRegisterLink = response.getPreRegisterLink();
        this.hashtag = response.getHashtag();
        this.thumbnails = response.getThumbnails();
        // popply 정보가 잘못되어서 위도 경도 바꿔 저장
        this.latitude = response.getLongitude();
        this.longitude = response.getLatitude();

        this.searchItems = response.getSearchItems();
        this.startDate = response.getStartDate().substring(0, 10);
        this.endDate = response.getEndDate().substring(0, 10);
        this.workingTime = response.getWorkingTime();
        this.status = response.getStatus();
    }

}

