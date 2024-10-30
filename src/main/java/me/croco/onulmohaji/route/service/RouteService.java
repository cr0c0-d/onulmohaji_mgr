package me.croco.onulmohaji.route.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.KakaoLocalService;
import me.croco.onulmohaji.exhibition.domain.Exhibition;
import me.croco.onulmohaji.exhibition.repository.ExhibitionRepository;
import me.croco.onulmohaji.facility.domain.Facility;
import me.croco.onulmohaji.facility.repository.FacilityRepository;
import me.croco.onulmohaji.festival.repository.FestivalRepository;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.member.repository.MemberRepository;
import me.croco.onulmohaji.place.repository.CustomPlaceRepository;
import me.croco.onulmohaji.popupstore.domain.Popupstore;
import me.croco.onulmohaji.popupstore.repository.PopupstoreRepository;
import me.croco.onulmohaji.route.domain.Route;
import me.croco.onulmohaji.route.domain.RouteDetail;
import me.croco.onulmohaji.route.domain.RoutePermission;
import me.croco.onulmohaji.route.dto.*;
import me.croco.onulmohaji.route.repository.RouteDetailRepository;
import me.croco.onulmohaji.route.repository.RoutePermissionRepository;
import me.croco.onulmohaji.route.repository.RouteRepository;
import me.croco.onulmohaji.util.HttpHeaderChecker;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final RouteDetailRepository routeDetailRepository;
    private final HttpHeaderChecker httpHeaderChecker;
    private final MemberRepository memberRepository;
    private final ExhibitionRepository exhibitionRepository;
    private final PopupstoreRepository popupstoreRepository;
    private final FacilityRepository facilityRepository;
    private final FestivalRepository festivalRepository;
    private final CustomPlaceRepository customPlaceRepository;
    private final RoutePermissionRepository routePermissionRepository;
    private final KakaoLocalService kakaoLocalService;

    public Long addRouteDetail(RouteDetailAddRequest addRequest, Member loginMember) {
        Route route = routeRepository.findRouteByDateAndUserId(addRequest.getDate(), loginMember.getId())
                .orElseGet(
                        () -> {
                            String[] date = addRequest.getDate().split("-");

                            return routeRepository.save(Route.builder()
                                    .userId(loginMember.getId())
                                    .title(Integer.parseInt(date[1]) + "월 " + Integer.parseInt(date[2]) + "일의 일정")
                                    .valid(1)
                                    .likeCnt(0)
                                    .routeDate(addRequest.getDate())
                                    .shareType(0)
                                    .build()
                            );
                        }
                );

        int maxOrder = routeRepository.findMaxOrderNoByRouteId(route.getId()).orElse(0);

        RouteDetail routeDetail = routeDetailRepository.save(RouteDetail.builder()
                        .orderNo(maxOrder + 1)
                        .placeId(addRequest.getPlaceId())
                        .routeId(route.getId())
                        .placeType(addRequest.getPlaceType())
                .build());

        return routeDetail.getRouteId();
        //return routeRepository.addRouteDetail(addRequest, loginMember);
    }

    public Route findRoute(Long userId, String date, HttpServletRequest request) {
        Route route = routeRepository.findRouteByDateAndUserId(date, userId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 route"));
//        Member loginMember = getLoginMember(request);
//        boolean isAdmin = loginMember.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch((authority -> authority.equals(Authorities.ROLE_ADMIN.getAuthorityName())));
//        switch (route.getShareType()) {
//
//            case 0 :    // 비공개
//                // 로그인 사용자의 route가 아니고, 로그인 사용자가 관리자가 아님
//                if(!route.getUserId().equals(loginMember.getId())
//                        && !isAdmin) {
//                    throw new AccessDeniedException("조회 권한 없음");
//                }
//                break;
//
//            case 1 :    // 제한 공유
//
//
//            case 2 :    // 완전 공유
//
//        }
        return route;
    }

    public Route findRouteById(Long routeId) {
        return routeRepository.findById(routeId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 route"));
    }

    public List<RouteDetailFindResponse> findRouteDetailListByRouteId(Long routeId) {
        List<RouteDetail> routeDetailList = routeDetailRepository.findByRouteIdOrderByOrderNo(routeId);
        return routeDetailList.stream().map(routeDetail -> {
            switch (routeDetail.getPlaceType()) {
                case "exhibition" :
                    return new RouteDetailFindResponse(routeDetail, exhibitionRepository.findById(Long.parseLong(routeDetail.getPlaceId())).get());

                case "popup" :
                    return new RouteDetailFindResponse(routeDetail, popupstoreRepository.findById(Long.parseLong(routeDetail.getPlaceId())).get());

                case "festival" :
                    return new RouteDetailFindResponse(routeDetail, festivalRepository.findById(routeDetail.getPlaceId()).get());

                case "custom" :
                    return new RouteDetailFindResponse(routeDetail, customPlaceRepository.findById(Long.parseLong(routeDetail.getPlaceId())).get());

                default:
                    return new RouteDetailFindResponse(routeDetail, facilityRepository.findById(Long.parseLong(routeDetail.getPlaceId())).get());
            }
        }).toList();
    }

    public List<String> getRouteMapUrlList(List<RouteDetailFindResponse> routeDetailList) {
        List<String> urlList = new ArrayList<>();

        for (int i = 0 ; i < routeDetailList.size() - 1; i++) {
            RouteDetailFindResponse thisRoute = routeDetailList.get(i);
            RouteDetailFindResponse nextRoute = routeDetailList.get(i+1);
            urlList.add("https://map.kakao.com/?map_type=TYPE_MAP&target=walk&rt=" +
                    thisRoute.getWpointx()+","+thisRoute.getWpointy()+","+nextRoute.getWpointx()+","+nextRoute.getWpointy()+
                    "&rt1="+thisRoute.getPlaceName().replace(" ", "+") +
                    "&rt2=" + nextRoute.getPlaceName().replace(" ", "+"));

        }
        return urlList;
    }

    public void updateRouteDetailOrder(List<RouteDetailUpdateRequest> routeDetailUpdateRequests) {
        routeRepository.updateRouteDetailOrder(routeDetailUpdateRequests);
    }

    public String findRouteMapUrlByLatitudeAndLongitude(RouteMapUrlFindRequest request) {
        Long startWpointx = null;
        Long startWpointy = null;
        Long endWpointx = null;
        Long endWpointy = null;

        String startPlaceType = request.getStartPlaceType();
        String endPlaceType = request.getEndPlaceType();

        switch (startPlaceType) {
            case "popup" :
                Popupstore popupstore = popupstoreRepository.findById(request.getStartPlaceId()).get();
                startWpointx = popupstore.getWpointx();
                startWpointy = popupstore.getWpointy();

                break;

            case "exhibition" :
                Exhibition exhibition = exhibitionRepository.findById(request.getStartPlaceId()).get();
                startWpointx = exhibition.getWpointx();
                startWpointy = exhibition.getWpointy();

                break;

            default:
                Facility facility = facilityRepository.findById(request.getStartPlaceId()).get();
                startWpointx = facility.getWpointx();
                startWpointy = facility.getWpointy();

        }

        switch (endPlaceType) {
            case "popup" :
                Popupstore popupstore = popupstoreRepository.findById(request.getStartPlaceId()).get();
                endWpointx = popupstore.getWpointx();
                endWpointy = popupstore.getWpointy();

                break;

            case "exhibition" :
                Exhibition exhibition = exhibitionRepository.findById(request.getStartPlaceId()).get();
                endWpointx = exhibition.getWpointx();
                endWpointy = exhibition.getWpointy();

                break;

            default:
                Facility facility = facilityRepository.findById(request.getStartPlaceId()).get();
                endWpointx = facility.getWpointx();
                endWpointy = facility.getWpointy();

        }
        String url = "https://map.kakao.com/?map_type=TYPE_MAP&target=walk&rt=" +
                startWpointx+","+startWpointy+","+endWpointx+","+endWpointy+
                "&rt1="+request.getStartPlaceName().replace(" ", "+") +
                "&rt2=" + request.getEndPlaceName().replace(" ", "+");
        return url;

    }

    public String findRouteMapUrlByWpointXAndY(RouteMapUrlFindRequest request) {
        return "https://map.kakao.com/?map_type=TYPE_MAP&target=walk&rt=" +
                request.getStartWpointX()+","+request.getStartWpointY()+","+request.getEndWpointX()+","+request.getEndWpointY()+
                "&rt1="+request.getStartPlaceName().replace(" ", "+") +
                "&rt2=" + request.getEndPlaceName().replace(" ", "+");
    }

    public void deleteRouteDetail(Long routeDetailId) {
        RouteDetail routeDetail = routeDetailRepository.findById(routeDetailId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 routeDetail"));
        routeDetailRepository.deleteById(routeDetailId);
        List<RouteDetail> routeDetailList = routeDetailRepository.findByRouteIdOrderByOrderNo(routeDetail.getRouteId());
        if(routeDetailList == null || routeDetailList.isEmpty()) {
            routeRepository.deleteById(routeDetail.getRouteId());
        } else {
            AtomicInteger index = new AtomicInteger(1);
            routeDetailList.forEach(detail -> {
                detail.setOrderNo(index.getAndIncrement());
            });
            routeDetailRepository.saveAll(routeDetailList);
        }
    }

    public String getRoutePermissionUrl(Long routeId, Member loginMember) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 routeId"));

        if(loginMember == null || !route.getUserId().equals(loginMember.getId())) { // 로그인 상태가 아니거나 route 생성자가 아닌 경우
            throw new AccessDeniedException("권한 없음");
        } else {
            if(route.getShareCode() == null) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                route.setShareCode(bCryptPasswordEncoder.encode(String.valueOf(routeId)).replaceAll("/", ""));
                routeRepository.save(route);
            }
            return route.getShareCode();
        }

    }

    public RoutePermissionInfoResponse getRouteInfoByShareCode(String shareCode) {
        Route route = routeRepository.findRouteByShareCode(shareCode).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 shareCode"));
        Member member = memberRepository.findById(route.getUserId()).get();
        List<RoutePermission> permissionList = routeRepository.findPermissionListByRouteId(route.getId());

        return new RoutePermissionInfoResponse(route, member.getNickname(), permissionList);
    }

    public RoutePermission addRoutePermission(RoutePermissionAddRequest request) {
        Route targetRoute = routeRepository.findById(request.getRouteId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 routeId"));

        // 기존 해당 날짜 해당 회원의 route 삭제
        // 회원이 만든 route 삭제
        routeRepository.deleteRouteByDateAndUserId(targetRoute.getRouteDate(), request.getUserId());

        // 회원이 참여중이던 route permission 삭제
        routeRepository.deleteRoutePermissionByDateAndUserId(targetRoute.getRouteDate(), request.getUserId());

        RoutePermission routePermission = RoutePermission.builder()
                                            .routeId(targetRoute.getId())
                                            .routeDate(targetRoute.getRouteDate())
                                            .userId(request.getUserId())
                                            .build();
        return routePermissionRepository.save(routePermission);

    }

    public List<Route> findRouteDateList(Long userId) {

        return routeRepository.findRouteListByUserId(userId);

//        public Map<Integer, Map<Integer, List<Integer>>> findRouteDateList(Long userId) {
//            Map<Integer, Map<Integer, List<Integer>>> routeDateMap = new HashMap<>();
//
//            List<Route> routeList = routeRepository.findRouteListByUserId(userId);
//            routeList.stream().forEach(route -> {
//                String routeDate = route.getRouteDate();
//                String[] dateArr = routeDate.split("-");
//                Map<Integer, List<Integer>> yearMap = routeDateMap.getOrDefault(Integer.parseInt(dateArr[0]), new HashMap<>());
//                List<Integer> dayList = yearMap.getOrDefault(Integer.parseInt(dateArr[1]), new ArrayList<>());
//                dayList.add(Integer.parseInt(dateArr[2]));
//                yearMap.put(Integer.parseInt(dateArr[1]), dayList);
//                routeDateMap.put(Integer.parseInt(dateArr[0]), yearMap);
//            });
//            return routeDateMap;
//        }
    }

    public List<Route> findRouteListByUserId(Long userId) {
        return routeRepository.findRouteListByUserId(userId);
    }

    @Transactional
    public Route updateRouteTitle(RouteTitleUpdateRequest request, Member loginMember) {
        Route route = routeRepository.findById(request.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 route"));
        List<RoutePermission> permissionList = routeRepository.findPermissionListByRouteId(request.getId());
        permissionList = permissionList.stream().filter(routePermission -> routePermission.getUserId().equals(loginMember.getId())).toList();
        if(route.getUserId().equals(loginMember.getId()) || !permissionList.isEmpty()) { // 권한이 있으면
            route.setTitle(request.getTitle());
            return route;
        } else {
            throw new AccessDeniedException("권한 없음");
        }
    }

    public List<Member> findRoutePermissionMemberList(Long routeId) {
        List<RoutePermission> routePermissionList = routeRepository.findPermissionListByRouteId(routeId);
        return routePermissionList.stream().map(routePermission -> memberRepository.findById(routePermission.getUserId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 id"))).toList();
    }
}
