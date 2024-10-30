package me.croco.onulmohaji.popupstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopupstoreDetail {

    @Id
    @Column(name = "store_detail_id")
    private Long storeDetailId;

    @Column(name = "store_id")
    private Long storeId;

    @Column
    private String contents;

    @Column
    private String images;

    @Column
    private String notice;

    @Column(name = "brand_url")
    private String brandUrl;

    @Column(name = "insta_url")
    private String instaUrl;

    @Column
    private boolean parking;

    @Column
    private boolean free;

    @Column(name = "no_kids")
    private boolean noKids;

    @Column
    private boolean food;

    @Column
    private boolean pet;

    @Column
    private boolean adult;

    @Column
    private boolean wifi;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;


}
