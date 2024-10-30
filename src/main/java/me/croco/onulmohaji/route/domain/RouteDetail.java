package me.croco.onulmohaji.route.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RouteDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "order_no")
    private int orderNo;

    @Column(name = "place_id")
    private String placeId;

    @Column(name = "place_type")
    private String placeType;



}
