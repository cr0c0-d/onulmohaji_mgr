package me.croco.onulmohaji.facility.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FacilityListFindResponse {
    private String type;
    private String typeName;
    private List<FacilityFindResponse> facilityList;

    public FacilityListFindResponse(String type, String typeName, List<FacilityFindResponse> facilityFindResponseList) {
        this.type = type;
        this.typeName = typeName;
        this.facilityList = facilityFindResponseList;
    }
}
