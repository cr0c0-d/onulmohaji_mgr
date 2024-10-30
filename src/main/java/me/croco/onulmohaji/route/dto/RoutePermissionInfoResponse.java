package me.croco.onulmohaji.route.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.croco.onulmohaji.route.domain.Route;
import me.croco.onulmohaji.route.domain.RoutePermission;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoutePermissionInfoResponse {
    private Long routeId;
    private String routeDate;
    private String ownerName;
    private List<Long> memberList;

    public RoutePermissionInfoResponse(Route route, String ownerName, List<RoutePermission> permissionList) {
        this.routeId = route.getId();
        this.routeDate = route.getRouteDate();
        this.ownerName = ownerName;

        List<Long> memberList = new ArrayList<>();
        memberList.add(route.getUserId());
        memberList.addAll(permissionList.stream().map(RoutePermission::getUserId).toList());
        this.memberList = memberList;
    }

}
