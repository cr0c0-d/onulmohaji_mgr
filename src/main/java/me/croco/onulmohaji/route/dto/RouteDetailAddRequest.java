package me.croco.onulmohaji.route.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RouteDetailAddRequest {
    private String placeId;
    private String placeType;
    private String date;
}
