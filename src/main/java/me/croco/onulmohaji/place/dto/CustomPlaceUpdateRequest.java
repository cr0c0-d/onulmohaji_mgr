package me.croco.onulmohaji.place.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomPlaceUpdateRequest {
    private Long id;
    private String name;
    private String address;
    private String addressRoad;
    private Double latitude;
    private Double longitude;
    private Long wpointx;
    private Long wpointy;
}
