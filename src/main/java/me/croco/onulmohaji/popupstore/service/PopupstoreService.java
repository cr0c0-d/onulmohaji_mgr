package me.croco.onulmohaji.popupstore.service;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.KakaoLocalService;
import me.croco.onulmohaji.api.dto.PopplyPopupstoreFindResponse;
import me.croco.onulmohaji.popupstore.domain.Popupstore;
import me.croco.onulmohaji.popupstore.domain.PopupstoreDetail;
import me.croco.onulmohaji.popupstore.domain.PopupstoreImage;
import me.croco.onulmohaji.popupstore.repository.PopupstoreDetailRepository;
import me.croco.onulmohaji.popupstore.repository.PopupstoreImageRepository;
import me.croco.onulmohaji.popupstore.repository.PopupstoreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopupstoreService {

    private final PopupstoreRepository popupstoreRepository;
    private final PopupstoreDetailRepository popupstoreDetailRepository;
    private final PopupstoreImageRepository popupstoreImageRepository;
    private final KakaoLocalService kakaoLocalService;

    // 팝업스토어 저장
    public void savePopupstoreInfo(List<PopplyPopupstoreFindResponse> storeList) {
        storeList.forEach(response -> {
            Popupstore popupstore = new Popupstore(response);

            //wpointx, y,를 구해 저장하는 과정
            List<Long> wpointlist = kakaoLocalService.getTranscoord(popupstore.getLongitude(), popupstore.getLatitude());
            popupstore.setWpointx(wpointlist.get(0));
            popupstore.setWpointy(wpointlist.get(1));

            popupstoreRepository.save(popupstore);
            popupstoreDetailRepository.save(response.getStoreDetail());
            response.getStoreImage().forEach(popupstoreImageRepository::save);
        });
    }

    public List<Popupstore> findPopupstoreListByDate(String keyword, String date, Double latitude, Double longitude, int distance) {
        return popupstoreRepository.findPopupstoreListByDate(keyword, date, latitude, longitude, distance);
    }

    public Popupstore findPopupstoreById(String id) {
        return popupstoreRepository.findById(Long.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 popupstore id : " + id));
    }
    public PopupstoreDetail findPopupstoreDetailById(String id) {
        return popupstoreDetailRepository.findByStoreId(Long.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 popupstore id : " + id));
    }

    public List<PopupstoreImage> findPopupstoreImagesById(String id) {
        return popupstoreImageRepository.findByStoreIdOrderByStoreImageId(Long.valueOf(id));
    }
}
