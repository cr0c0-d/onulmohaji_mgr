package me.croco.onulmohaji.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import me.croco.onulmohaji.popupstore.domain.PopupstoreDetail;
import me.croco.onulmohaji.popupstore.domain.PopupstoreImage;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PopplyPopupstoreFindResponse {

    private Long storeId;

    private String name;

    private int categoryId;

    private String mainBrand;

    private String brand_1;

    private String brand_2;

    private String title;

    private String topLevelAddress;

    private String address;

    private boolean preRegister;

    private String preRegisterStartDate;

    private String preRegisterEndDate;

    private String preRegisterLink;

    private String hashtag;

    private String thumbnails;

    private Double latitude;

    private Double longitude;

    private String searchItems;

    private String startDate;

    private String endDate;

    private String workingTime;

    private String status;

    private PopupstoreDetail storeDetail;

    private List<PopupstoreImage> storeImage;
}
