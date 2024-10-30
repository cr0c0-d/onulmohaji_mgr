package me.croco.onulmohaji.facility.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.KakaoLocalService;
import me.croco.onulmohaji.facility.domain.Facility;
import me.croco.onulmohaji.facility.dto.FacilityFindResponse;
import me.croco.onulmohaji.facility.dto.FacilityListFindResponse;
import me.croco.onulmohaji.facility.repository.FacilityRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final KakaoLocalService kakaoLocalService;

    public List<Facility> getNewFacilities(String keyword, String categoryId, Double latitude, Double longitude) {
        List<Facility> facilityList = kakaoLocalService.getLocalListByCategory(keyword, categoryId, latitude, longitude);
        return facilityRepository.saveAll(facilityList);
    }

//    public List<Facility> findLocalFacilityList(String keyword, Double latitude, Double longitude) {
//
//
//        List<Facility> facilityList = null;
//        if(keyword == null) {
//            facilityList = facilityRepository.findDefaultFacilityList(latitude, longitude);
//
//            // DB에 저장된 facility가 100개 미만인 경우 카카오맵에서 가져와서 저장하기
//            if(facilityList.size() < 100) {
//                CompletableFuture.runAsync(() -> {
//                    facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory("이색데이트", null, latitude, longitude));
//                    facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory("실내놀거리", null, latitude, longitude));
//                    facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory("테마파크", null, latitude, longitude));
//                    facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory("테마카페", null, latitude, longitude));
//                    facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory("문화", null, latitude, longitude));
//                    facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory("산책", null, latitude, longitude));
//                    facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory("레저", null, latitude, longitude));
//                    facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory("명소", null, latitude, longitude));
//                });
//            }
//        } else {
//            //facilityList = facilityRepository.findFacilityListByKeyword(keyword, latitude, longitude);
//            facilityList = facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory(keyword, null, latitude, longitude));
//        }
//        return facilityList;
//    }
    public List<FacilityListFindResponse> findLocalFacilityList(String keyword, Double latitude, Double longitude, int distance) {
        List<FacilityListFindResponse> responses = new ArrayList<>();

        /**
         * 식당, 테마카페, 관광명소, 문화예술, 실내놀거리
         */
        if(keyword == null) {

            Map<String, String> categoryList = new HashMap<>();

            // 표시이름 : 검색어
            categoryList.put("food", "음식점");
            categoryList.put("cafe", "테마카페");
            categoryList.put("attraction", "관광,명소");
            categoryList.put("art", "문화,예술");
            categoryList.put("indoor", "실내놀거리");

            Iterator<String> it = categoryList.keySet().iterator();
            String type = it.next();

            while(true) {
                List<FacilityFindResponse> facilityList = facilityRepository.findFacilityListByCategory(categoryList.get(type), latitude, longitude, distance).stream().map(facility -> new FacilityFindResponse(facility, latitude, longitude)).toList();
                responses.add(new FacilityListFindResponse(type, categoryList.get(type), facilityList));

                if(it.hasNext()) {
                    type = it.next();
                } else {
                    break;
                }
            }

            responses.stream().forEach(facilityListFindResponse -> {
                if(facilityListFindResponse.getFacilityList().size() < 50) {
                    CompletableFuture.runAsync(() -> {
                        facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory(facilityListFindResponse.getTypeName(), null, latitude, longitude));
                    });
                }
            });


        } else {
           List<Facility> facilityList = facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory(keyword, null, latitude, longitude));
           responses.add(new FacilityListFindResponse("etc", "기타 장소", facilityList.stream().map(facility -> new FacilityFindResponse(facility, latitude, longitude)).toList()));
        }

        return responses;
    }

    public List<Facility> findFoodListByPlace(Double latitude, Double longitude) {
        CompletableFuture.runAsync(() -> facilityRepository.saveAll(kakaoLocalService.getLocalListByCategory(null, "FD6", latitude, longitude)));

        return facilityRepository.findFoodListByPlace(latitude, longitude);
    }

    public Facility findFacilityById(String id) {
        return facilityRepository.findById(Long.valueOf(id)).orElse(null);
    }
}
