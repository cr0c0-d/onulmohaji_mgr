package me.croco.onulmohaji.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberSearchInfo {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "localcode_id")
    private Long localcodeId = Long.parseLong("11680");

    @Column
    private int distance = 3000;

    // 카테고리 필터, 비활성화한 카테고리만 JSON 형식으로 추가 { "카테고리명" : false }
    @Column(name = "category_filter")
    private String categoryFilter = "{}";

    @Column(name = "default_date")
    private String defaultDate = "today";
    
    public MemberSearchInfo(Long id, Long localcodeId, int distance, String categoryFilter, String defaultDate) {
        this.id = id;
        this.localcodeId = localcodeId;
        this.distance = distance;
        this.categoryFilter = categoryFilter;
        this.defaultDate = defaultDate;
    }

    public MemberSearchInfo(Long id) {
        this.id = id;
    }
}
