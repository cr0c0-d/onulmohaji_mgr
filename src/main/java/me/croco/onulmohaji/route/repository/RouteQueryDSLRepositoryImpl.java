package me.croco.onulmohaji.route.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.route.domain.*;
import me.croco.onulmohaji.route.dto.RouteDetailAddRequest;
import me.croco.onulmohaji.route.dto.RouteDetailUpdateRequest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RouteQueryDSLRepositoryImpl implements RouteQueryDSLRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QRoute qRoute = QRoute.route;
    private final QRoutePermission qRoutePermission = QRoutePermission.routePermission;
    private final QRouteDetail qRouteDetail = QRouteDetail.routeDetail;


    @Override
    public Optional<Route> findRouteByDateAndUserId(String date, Long userId) throws IllegalArgumentException {

        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(qRoute)
                        .where(qRoute.routeDate.eq(date).and(
                                qRoute.userId.eq(userId).or(qRoutePermission.userId.eq(userId))
                        )
                )
                .leftJoin(qRoutePermission)
                .on(qRoute.id.eq(qRoutePermission.routeId))
                .fetchOne()
        );
    }

    @Override
    public Optional<Integer> findMaxOrderNoByRouteId(Long routeId) {
        return Optional.ofNullable(
                jpaQueryFactory.select(qRouteDetail.orderNo.max())
                        .from(qRouteDetail)
                        .where(qRouteDetail.routeId.eq(routeId))
                .fetchOne()
        );
    }

    @Override
    @Transactional
    public void updateRouteDetailOrder(List<RouteDetailUpdateRequest> routeDetailUpdateRequests) {
        routeDetailUpdateRequests.forEach(routeDetailUpdateRequest -> {
            jpaQueryFactory.update(qRouteDetail)
                    .set(qRouteDetail.orderNo, routeDetailUpdateRequest.getOrderNo())
                    .where(qRouteDetail.id.eq(routeDetailUpdateRequest.getId()))
                    .execute();
        });
    }

    @Override
    public Optional<Route> findRouteByShareCode(String shareCode) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(qRoute)
                .where(qRoute.shareCode.eq(shareCode))
                .fetchOne()
        );
    }

    @Override
    public List<RoutePermission> findPermissionListByRouteId(Long routeId) {
        return jpaQueryFactory.selectFrom(qRoutePermission)
                .where(qRoutePermission.routeId.eq(routeId))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteRouteByDateAndUserId(String date, Long userId) {
        jpaQueryFactory.delete(qRoute)
                .where(qRoute.userId.eq(userId)
                        .and(qRoute.routeDate.eq(date))
                )
                .execute();

    }

    @Override
    @Transactional
    public void deleteRoutePermissionByDateAndUserId(String date, Long userId) {
        jpaQueryFactory.delete(qRoutePermission)
                .where(qRoutePermission.routeDate.eq(date)
                        .and(qRoutePermission.userId.eq(userId))
                )
                .execute();
    }

    @Override
    public List<Route> findRouteListByUserId(Long userId) {
        return jpaQueryFactory.selectFrom(qRoute)
                .where(qRoute.userId.eq(userId)
                        .or(qRoutePermission.userId.eq(userId)))
                .leftJoin(qRoutePermission)
                .on(qRoute.id.eq(qRoutePermission.routeId))
                .orderBy(qRoute.routeDate.desc())
                .fetch();
    }

    @Override
    public String findNearestRouteDateByUserId(Long userId) {
        String todayStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return jpaQueryFactory.select(qRoute.routeDate)
                .from(qRoute)
                .where(
                        (qRoute.userId.eq(userId).or(qRoutePermission.userId.eq(userId)))
                                .and(qRoute.routeDate.gt(todayStr).or(qRoute.routeDate.eq(todayStr)))
                )
                .leftJoin(qRoutePermission)
                .on(qRoute.id.eq(qRoutePermission.routeId))
                .fetchOne();
    }


}
