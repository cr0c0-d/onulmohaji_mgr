package me.croco.onulmohaji.route.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.route.domain.RouteDetail;

@Setter
@Getter
public class RouteDetailUpdateRequest {
    private Long id;

    private Long routeId;

    private int orderNo;

    private String placeId;

    private String placeType;

    private String placeName;

    private String placeTypeName;

    private String thumbnail;

    private Double latitude;

    private Double longitude;

    private String address;

}
