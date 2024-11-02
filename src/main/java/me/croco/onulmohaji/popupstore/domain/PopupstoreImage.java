package me.croco.onulmohaji.popupstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopupstoreImage {

    @Id
    @Column(name = "store_image_id")
    private Long storeImageId;

    @Column(name = "store_id")
    private Long storeId;

    @Column
    private String url;
}
