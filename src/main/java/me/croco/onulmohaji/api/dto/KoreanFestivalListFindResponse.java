package me.croco.onulmohaji.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KoreanFestivalListFindResponse {
    private String fstvlCntntsId;
    private String dispFstvlCntntsImgRout;
    private Double ycrdVal;
    private Double xcrdVal;
    private String fstvlOutlCn;
    private String fstvlCrmnCn;
    private String cntntsNm;
    private String fstvlBgngDe;
    private String fstvlEndDe;
    private String adres;
    private String areaNm;
    private String fstvlUtztFareInfo;
    private String instaUrl;
    private String fstvlAspcsTelno;
    private String fstvlHmpgUrl;
}
