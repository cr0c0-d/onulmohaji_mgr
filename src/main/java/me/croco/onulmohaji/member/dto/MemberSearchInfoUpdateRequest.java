package me.croco.onulmohaji.member.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberSearchInfoUpdateRequest {
    private Long id;

    private Long localcodeId;

    private int distance;

    // 카테고리 필터, 비활성화한 카테고리만 JSON 형식으로 추가 { "카테고리명" : false }
    private String categoryFilter;

    private String defaultDate;

    private String defaultDateValue;
}
