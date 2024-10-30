package me.croco.onulmohaji.place.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.KakaoLocalService;
import me.croco.onulmohaji.member.domain.Member;
import me.croco.onulmohaji.place.domain.CustomPlace;
import me.croco.onulmohaji.place.dto.CustomPlaceAddRequest;
import me.croco.onulmohaji.place.dto.CustomPlaceUpdateRequest;
import me.croco.onulmohaji.place.repository.CustomPlaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomPlaceService {

    private final CustomPlaceRepository customPlaceRepository;
    private final KakaoLocalService kakaoLocalService;

    public CustomPlace addCustomPlace(CustomPlaceAddRequest addRequest, Member loginMember) {
        CustomPlace customPlace = CustomPlace.builder()
                .userId(loginMember.getId())
                .name(addRequest.getName())
                .address(addRequest.getAddress())
                .addressRoad(addRequest.getAddressRoad())
                .latitude(addRequest.getLatitude())
                .longitude(addRequest.getLongitude())
                .build();

        List<Long> wpointList = kakaoLocalService.getTranscoord(customPlace.getLongitude(), customPlace.getLatitude());

        customPlace.setWpointx(wpointList.get(0));
        customPlace.setWpointy(wpointList.get(1));

        return customPlaceRepository.save(customPlace);
    }

    public List<CustomPlace> findCustomPlaceListByUserId(Long userId) {
        return customPlaceRepository.findCustomPlaceListByUserId(userId);
    }

    @Transactional
    public CustomPlace updateCustomPlace(CustomPlaceUpdateRequest updateRequest, Member loginMember) {
        CustomPlace customPlace = customPlaceRepository.findById(updateRequest.getId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 id"));


        List<Long> wpointList = kakaoLocalService.getTranscoord(customPlace.getLongitude(), customPlace.getLatitude());

        updateRequest.setWpointx(wpointList.get(0));
        updateRequest.setWpointy(wpointList.get(1));

        return customPlace.update(updateRequest);
    }

    public void deleteCustomPlace(Long placeId, Member loginMember) throws Exception {
        CustomPlace customPlace = customPlaceRepository.findById(placeId).get();

        if(customPlace.getUserId().equals(loginMember.getId())) {   // 로그인 유저의 custom place
            customPlaceRepository.deleteById(placeId);
        } else {
            throw new Exception("권한 없음");
        }

    }

}
