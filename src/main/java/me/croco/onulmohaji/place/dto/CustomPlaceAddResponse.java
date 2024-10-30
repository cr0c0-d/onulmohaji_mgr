package me.croco.onulmohaji.place.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.place.domain.CustomPlace;

@Setter
@Getter
public class CustomPlaceAddResponse {
    private Long id;
    private String name;
    private String address;
    private String address_road;
    private Double latitude;
    private Double longitude;

    public CustomPlaceAddResponse(CustomPlace customPlace) {
        this.id = customPlace.getId();
        this.name = customPlace.getName();
        this.address = customPlace.getAddress();
        this.address_road = customPlace.getAddressRoad();
        this.latitude = customPlace.getLatitude();
        this.longitude = customPlace.getLongitude();
    }
}
