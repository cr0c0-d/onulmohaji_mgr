package me.croco.onulmohaji.route.dto;

import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.route.domain.Route;
import me.croco.onulmohaji.route.domain.RoutePermission;

import java.util.List;

@Setter
@Getter
public class RouteListFindResponse {
    private Long id;
    private String title;
    private String date;
    private List<String> memberList;

    public RouteListFindResponse(Route route, List<Member> memberList) {
        this.id = route.getId();
        this.title = route.getTitle();
        this.date = route.getRouteDate();
        this.memberList = memberList.stream().map(Member::getNickname).toList();
    }
}
