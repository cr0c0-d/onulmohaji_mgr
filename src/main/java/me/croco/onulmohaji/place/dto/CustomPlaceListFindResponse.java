package me.croco.onulmohaji.place.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.place.domain.CustomPlace;

@Setter
@Getter
public class CustomPlaceListFindResponse {
    private Long placeId;
    private String placeName;
    private String placeType;
    private String address;
    private String addressRoad;
    private Double latitude;
    private Double longitude;


    public CustomPlaceListFindResponse(CustomPlace customPlace) {
        this.placeId = customPlace.getId();
        this.placeName = customPlace.getName();
        this.placeType = "custom";
        this.address = customPlace.getAddress();
        this.addressRoad = customPlace.getAddressRoad();
        this.latitude = customPlace.getLatitude();
        this.longitude = customPlace.getLongitude();
    }
}
