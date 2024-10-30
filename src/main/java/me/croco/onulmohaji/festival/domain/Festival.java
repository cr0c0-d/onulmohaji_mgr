package me.croco.onulmohaji.festival.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.croco.onulmohaji.api.dto.KoreanFestivalListFindResponse;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Festival {
    @Id
    @Column
    private String id;

    @Column
    private String thumbnail;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private String contents1;

    @Column
    private String contents2;

    @Column
    private String title;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column
    private String address;

    @Column(name = "area_nm")
    private String areaNm;

    @Column
    private String fare;

    @Column(name = "insta_url")
    private String instaUrl;

    @Column
    private String contact;

    @Column
    private String homepageUrl;

    @Column
    private Long wpointx;

    @Column
    private Long wpointy;

    public Festival(KoreanFestivalListFindResponse response) {
        this.id = response.getFstvlCntntsId();
        this.thumbnail = response.getDispFstvlCntntsImgRout().replace("/data/kfes/", "https://kfescdn.visitkorea.or.kr/kfes/upload/");
        this.latitude = response.getXcrdVal();
        this.longitude = response.getYcrdVal();
        this.contents1 = response.getFstvlOutlCn();
        this.contents2 = response.getFstvlCrmnCn();
        this.title = response.getCntntsNm();
        this.startDate = response.getFstvlBgngDe().replace(".","-");
        this.endDate = response.getFstvlEndDe().replace(".","-");
        this.address = response.getAdres();
        this.areaNm = response.getAreaNm();
        this.fare = response.getFstvlUtztFareInfo();
        this.instaUrl = response.getInstaUrl();
        this.contact = response.getFstvlAspcsTelno();
        this.homepageUrl = response.getFstvlHmpgUrl();
    }

}
