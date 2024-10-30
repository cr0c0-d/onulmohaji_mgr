package me.croco.onulmohaji.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.exhibition.domain.Exhibition;
import me.croco.onulmohaji.festival.domain.Festival;
import me.croco.onulmohaji.popupstore.domain.Popupstore;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class PlaceListFindResponse {
    private String placeId;
    private int categoryId;
    private String category;
    private String placeName;
    private String placeType;
    private String placeTypeName;
    private String address_short;
    private String address;
    private boolean preRegister;
    private String preRegisterStartDate;
    private String preRegisterEndDate;
    private String preRegisterLink;
    private String thumbnail;
    private Double latitude;
    private Double longitude;
    private String startDate;
    private String endDate;
    private String placeUrl;
    private int distance;

    public PlaceListFindResponse(Popupstore popupstore, Double latitude, Double longitude) {
        this.placeId = String.valueOf(popupstore.getStoreId());
        this.placeName = HtmlUtils.htmlUnescape(popupstore.getTitle());
        this.categoryId = popupstore.getCategoryId();
        this.address_short = popupstore.getTopLevelAddress();
        this.address = popupstore.getAddress();
        this.preRegister = popupstore.isPreRegister();
        this.preRegisterStartDate = popupstore.getPreRegisterStartDate();
        this.preRegisterEndDate = popupstore.getPreRegisterEndDate();
        this.preRegisterLink = popupstore.getPreRegisterLink();
        this.thumbnail = popupstore.getThumbnails();
        this.latitude = popupstore.getLatitude();
        this.longitude = popupstore.getLongitude();
        this.startDate = popupstore.getStartDate();
        this.endDate = popupstore.getEndDate();
        this.placeType = "popup";
        this.placeTypeName = "팝업스토어";
        this.placeUrl = "/popup/" + popupstore.getStoreId();
        this.distance = getDistance(latitude, longitude, popupstore.getLatitude(), popupstore.getLongitude());
    }

    public PlaceListFindResponse(Exhibition exhibition, Double latitude, Double longitude) {
        this.placeId = String.valueOf(exhibition.getSeq());
        this.placeName = HtmlUtils.htmlUnescape(exhibition.getTitle());
        this.startDate = LocalDate.parse(exhibition.getStartDate()).format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        this.endDate = LocalDate.parse(exhibition.getEndDate()).format(DateTimeFormatter.ofPattern("yy-MM-dd"));
        this.address_short = exhibition.getArea() + " " + exhibition.getPlace();
        this.category = exhibition.getRealmName();
        this.thumbnail = exhibition.getThumbnail();
        this.latitude = exhibition.getGpsX();
        this.longitude = exhibition.getGpsY();
        this.placeType = "exhibition";
        this.placeTypeName = "전시회/공연";
        this.placeUrl = "/exhibition/" + exhibition.getSeq();
        this.distance = getDistance(latitude, longitude, exhibition.getGpsY(), exhibition.getGpsX());
    }

    public PlaceListFindResponse(Festival festival, Double latitude, Double longitude) {
        this.placeId = festival.getId();
        this.placeName = festival.getTitle();
        this.address_short = festival.getAreaNm();
        this.address = festival.getAddress();
        this.thumbnail = festival.getThumbnail();
        this.latitude = festival.getLatitude();
        this.longitude = festival.getLongitude();
        this.startDate = festival.getStartDate();
        this.endDate = festival.getEndDate();
        this.placeType = "festival";
        this.placeTypeName = "축제";
        this.placeUrl = "/festival/" + festival.getId();
        this.distance = getDistance(latitude, longitude, festival.getLatitude(), festival.getLongitude());
    }

    int getDistance(Double latitude, Double longitude, Double latitude_2, Double longitude_2) {
        final int R = 6371_000; // 지구의 반지름 (미터)

        // 위도를 라디안으로 변환
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(latitude_2);
        double deltaLat = Math.toRadians(latitude_2 - latitude);
        double deltaLon = Math.toRadians(longitude_2 - longitude);

        // 하버사인 공식
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 거리 계산
        return (int) (R * c); // 미터 단위로 반환
    }
}
