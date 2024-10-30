package me.croco.onulmohaji.route.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "route_date")
    private String routeDate;

    @Column(name = "title")
    private String title;

    @Column(name = "like_cnt")
    private int likeCnt;

    @Column(name = "share_type")
    private int shareType;

    @Column(name = "share_code")
    private String shareCode;

    @Column
    private int valid;



}
