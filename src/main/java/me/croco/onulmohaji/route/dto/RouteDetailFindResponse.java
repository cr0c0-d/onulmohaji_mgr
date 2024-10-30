package me.croco.onulmohaji.route.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.exhibition.domain.Exhibition;
import me.croco.onulmohaji.facility.domain.Facility;
import me.croco.onulmohaji.festival.domain.Festival;
import me.croco.onulmohaji.place.domain.CustomPlace;
import me.croco.onulmohaji.popupstore.domain.Popupstore;
import me.croco.onulmohaji.route.domain.RouteDetail;

@Setter
@Getter
public class RouteDetailFindResponse {

    private Long id;

    private Long routeId;

    private int orderNo;

    private String placeId;

    private String placeType;

    private String placeName;

    private String placeTypeName;

    private String thumbnail;

    private Long wpointx;

    private Long wpointy;

    private Double latitude;

    private Double longitude;

    private String address;

    private String addressRoad;

    private String placeUrl;

    public RouteDetailFindResponse(RouteDetail routeDetail, Festival festival) {
        this.id = routeDetail.getId();
        this.routeId = routeDetail.getRouteId();
        this.orderNo = routeDetail.getOrderNo();
        this.placeId = routeDetail.getPlaceId();
        this.placeType = routeDetail.getPlaceType();

        this.thumbnail = festival.getThumbnail();
        this.placeName = festival.getTitle();
        this.placeTypeName = "축제";
        this.wpointx = festival.getWpointx();
        this.wpointy = festival.getWpointy();
        this.latitude = festival.getLatitude();
        this.longitude = festival.getLongitude();
        this.placeUrl = "/festival/" + festival.getId();
    }

    public RouteDetailFindResponse(RouteDetail routeDetail, Exhibition exhibition) {
        this.id = routeDetail.getId();
        this.routeId = routeDetail.getRouteId();
        this.orderNo = routeDetail.getOrderNo();
        this.placeId = routeDetail.getPlaceId();
        this.placeType = routeDetail.getPlaceType();

        this.thumbnail = exhibition.getThumbnail();
        this.placeName = exhibition.getTitle();
        this.placeTypeName = "전시회 / 공연";
        this.wpointx = exhibition.getWpointx();
        this.wpointy = exhibition.getWpointy();
        this.latitude = exhibition.getGpsY();
        this.longitude = exhibition.getGpsX();
        this.placeUrl = "/exhibition/" + exhibition.getSeq();
    }

    public RouteDetailFindResponse(RouteDetail routeDetail, Popupstore popupstore) {
        this.id = routeDetail.getId();
        this.routeId = routeDetail.getRouteId();
        this.orderNo = routeDetail.getOrderNo();
        this.placeId = routeDetail.getPlaceId();
        this.placeType = routeDetail.getPlaceType();

        this.thumbnail = popupstore.getThumbnails();
        this.placeName = popupstore.getTitle();
        this.placeTypeName = "팝업스토어";
        this.wpointx = popupstore.getWpointx();
        this.wpointy = popupstore.getWpointy();
        this.latitude = popupstore.getLatitude();
        this.longitude = popupstore.getLongitude();
        this.placeUrl = "/popup/" + popupstore.getStoreId();
    }

    public RouteDetailFindResponse(RouteDetail routeDetail, Facility facility) {
        this.id = routeDetail.getId();
        this.routeId = routeDetail.getRouteId();
        this.orderNo = routeDetail.getOrderNo();
        this.placeId = routeDetail.getPlaceId();
        this.placeType = routeDetail.getPlaceType();

        this.thumbnail = facility.getThumbnail();
        this.placeName = facility.getPlaceName();

        StringBuilder builder = new StringBuilder();
        builder.append(facility.getCategoryName());
        builder.reverse();
        builder.delete(builder.indexOf(" > "), builder.length());
        builder.reverse();
        this.placeTypeName = builder.toString();
        this.wpointx = facility.getWpointx();
        this.wpointy = facility.getWpointy();
        this.latitude = facility.getLatitude();
        this.longitude = facility.getLongitude();
        this.placeUrl = facility.getPlaceUrl();
    }

    public RouteDetailFindResponse(RouteDetail routeDetail, CustomPlace customPlace) {
        this.id = routeDetail.getId();
        this.routeId = routeDetail.getRouteId();
        this.orderNo = routeDetail.getOrderNo();
        this.placeId = routeDetail.getPlaceId();
        this.placeType = routeDetail.getPlaceType();

        this.placeName = customPlace.getName();

        this.placeTypeName = "나만의 장소";
        this.wpointx = customPlace.getWpointx();
        this.wpointy = customPlace.getWpointy();
        this.latitude = customPlace.getLatitude();
        this.longitude = customPlace.getLongitude();
        this.address = customPlace.getAddress();
        this.addressRoad = customPlace.getAddressRoad();
//        this.placeUrl = customPlace.getPlaceUrl();
    }
}
