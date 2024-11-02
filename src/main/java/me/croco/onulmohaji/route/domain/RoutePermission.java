package me.croco.onulmohaji.route.domain;

import jakarta.persistence.*;
import lombok.*;

@Data
public class RoutePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "route_date")
    private String routeDate;

    @Builder
    public RoutePermission(Long routeId, Long userId, String routeDate) {
        this.routeId = routeId;
        this.userId = userId;
        this.routeDate = routeDate;
    }
}
