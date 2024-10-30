package me.croco.onulmohaji.festival.service;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.KakaoLocalService;
import me.croco.onulmohaji.api.KoreanFestivalService;
import me.croco.onulmohaji.api.dto.KoreanFestivalListFindResponse;
import me.croco.onulmohaji.festival.domain.Festival;
import me.croco.onulmohaji.festival.repository.FestivalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final KoreanFestivalService koreanFestivalService;
    private final FestivalRepository festivalRepository;
    private final KakaoLocalService kakaoLocalService;

    public List<Festival> saveNewFestivalList() {
        List<KoreanFestivalListFindResponse> festivalList = koreanFestivalService.getFestivalList();
        List<Festival> savedList = festivalRepository.saveAll(festivalList.stream().map(Festival::new).toList());
        savedList.forEach(festival -> {
            if(festival.getLongitude() != null && festival.getLatitude() != null) {
            List<Long> wpointList = kakaoLocalService.getTranscoord(festival.getLongitude(), festival.getLatitude());
            festival.setWpointx(wpointList.get(0));
            festival.setWpointy(wpointList.get(1));
            }
        });
        return festivalRepository.saveAll(savedList);
    }
// 데이터가 정확하지 않아 사용하지 않음
//    public List<Festival> saveNewFestivalListByCalendar() throws Exception {
//        List<KoreanFestivalListFindResponse> festivalResponseList = koreanFestivalService.getFestivalListByCalendar();
//        List<Festival> festivalList = festivalRepository.saveAll(festivalResponseList.stream().map(Festival::new).toList());
//        festivalList = festivalList.stream().filter(festival -> festival.getLatitude() == null).map(festival -> {
//            List<Double> xyList = kakaoLocalService.getAddressInfo(festival.getAddress());
//            if(!xyList.isEmpty()) {
//                festival.setLongitude(xyList.get(0));
//                festival.setLatitude(xyList.get(1));
//            }
//            return festival;
//        }).toList();
//
//        return festivalRepository.saveAll(festivalList);
//    }

    public List<Festival> findFestivalListByDate(String keyword, String date, Double latitude, Double longitude, int distance) {
        return festivalRepository.findFestivalListByDate(keyword, date, latitude, longitude, distance);
    }

    public Festival findFestivalById(String id) {
        return festivalRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 festival id : " + id));
    }
}
