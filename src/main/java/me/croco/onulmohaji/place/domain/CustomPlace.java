package me.croco.onulmohaji.place.domain;

import jakarta.persistence.*;
import lombok.*;
import me.croco.onulmohaji.place.dto.CustomPlaceUpdateRequest;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Data
@EntityListeners(AuditingEntityListener.class)
public class CustomPlace {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column
    private String name;

    @Column
    private String address;

    @Column(name="address_road")
    private String addressRoad;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private Long wpointx;

    @Column
    private Long wpointy;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Builder
    public CustomPlace(Long userId, String name, String address, String addressRoad, Double latitude, Double longitude) {
        this.userId = userId;
        this.name = name;
        this.address = address;
        this.addressRoad = addressRoad;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public CustomPlace update(CustomPlaceUpdateRequest request) {
        this.name = request.getName();
        this.address = request.getAddress();
        this.addressRoad = request.getAddressRoad();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
        this.wpointx = request.getWpointx();
        this.wpointy = request.getWpointy();

        return this;
    }
}
