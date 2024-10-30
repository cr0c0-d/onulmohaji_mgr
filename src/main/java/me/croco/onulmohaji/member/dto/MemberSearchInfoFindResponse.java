package me.croco.onulmohaji.member.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.member.domain.MemberSearchInfo;

@Setter
@Getter
public class MemberSearchInfoFindResponse {
    private Long id;

    private Long localcodeId = Long.parseLong("11680");

    private int distance = 3000;

    // 카테고리 필터, 비활성화한 카테고리만 JSON 형식으로 추가 { "카테고리명" : false }
    private String categoryFilter = "{}";

    private String defaultDate = "today";
    private String defaultDateValue;

    public MemberSearchInfoFindResponse(MemberSearchInfo memberSearchInfo, String defaultDateValue) {
        this.id = memberSearchInfo.getId();
        this.localcodeId = memberSearchInfo.getLocalcodeId();
        this.distance = memberSearchInfo.getDistance();
        this.categoryFilter = memberSearchInfo.getCategoryFilter();
        this.defaultDate = memberSearchInfo.getDefaultDate();
        this.defaultDateValue = defaultDateValue;
    }
}
