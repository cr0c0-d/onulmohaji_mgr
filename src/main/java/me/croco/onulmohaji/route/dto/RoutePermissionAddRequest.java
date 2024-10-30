package me.croco.onulmohaji.route.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoutePermissionAddRequest {
    private Long routeId;
    private Long userId;
}
