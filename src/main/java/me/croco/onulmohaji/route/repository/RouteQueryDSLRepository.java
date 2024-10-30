package me.croco.onulmohaji.route.repository;

import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.route.domain.Route;
import me.croco.onulmohaji.route.domain.RoutePermission;
import me.croco.onulmohaji.route.dto.RouteDetailAddRequest;
import me.croco.onulmohaji.route.dto.RouteDetailUpdateRequest;

import java.util.List;
import java.util.Optional;

public interface RouteQueryDSLRepository {
    Optional<Route> findRouteByDateAndUserId(String date, Long userId);

    Optional<Integer> findMaxOrderNoByRouteId(Long routeId);

    void updateRouteDetailOrder(List<RouteDetailUpdateRequest> routeDetailUpdateRequests);

    Optional<Route> findRouteByShareCode(String shareCode);

    List<RoutePermission> findPermissionListByRouteId(Long routeId);

    void deleteRouteByDateAndUserId(String date, Long userId);

    void deleteRoutePermissionByDateAndUserId(String date, Long userId);

    List<Route> findRouteListByUserId(Long userId);

    String findNearestRouteDateByUserId(Long userId);
}
