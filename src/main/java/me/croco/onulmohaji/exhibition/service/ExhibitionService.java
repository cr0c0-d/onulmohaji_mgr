package me.croco.onulmohaji.exhibition.service;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.KakaoLocalService;
import me.croco.onulmohaji.exhibition.domain.Exhibition;
import me.croco.onulmohaji.exhibition.domain.ExhibitionDetail;
import me.croco.onulmohaji.exhibition.repository.ExhibitionDetailRepository;
import me.croco.onulmohaji.exhibition.repository.ExhibitionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionDetailRepository exhibitionDetailRepository;

    private final KakaoLocalService kakaoLocalService;

    public List<Exhibition> saveExhibitionFromSearchResult(List<Exhibition> exhibitionList) {
        exhibitionList = exhibitionList.stream().filter(exhibition -> exhibition.getGpsX() != null).toList();
        exhibitionList.forEach(exhibition -> {
            // yyyyMMdd 형식 문자열 -> yyyy-MM-dd 형식으로 바꿔 저장
            DateTimeFormatter before = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter after = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            exhibition.setStartDate(LocalDate.parse(exhibition.getStartDate(), before).format(after));
            exhibition.setEndDate(LocalDate.parse(exhibition.getEndDate(), before).format(after));

            exhibition.setTitle(HtmlUtils.htmlUnescape(exhibition.getTitle()));

            if(exhibition.getGpsX() != null && exhibition.getGpsY() != null) {
                List<Long> wpointlist = kakaoLocalService.getTranscoord(exhibition.getGpsX(), exhibition.getGpsY());
                exhibition.setWpointx(wpointlist.get(0));
                exhibition.setWpointy(wpointlist.get(1));
            }
        });
        return exhibitionRepository.saveAll(exhibitionList);
    }

    public void saveExhibitionDetailFromSearchResult(ExhibitionDetail exhibitionDetail) {
        // yyyyMMdd 형식 문자열 -> yyyy-MM-dd 형식으로 바꿔 저장
        DateTimeFormatter before = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter after = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        exhibitionDetail.setStartDate(LocalDate.parse(exhibitionDetail.getStartDate(), before).format(after));
        exhibitionDetail.setEndDate(LocalDate.parse(exhibitionDetail.getEndDate(), before).format(after));

        exhibitionDetail.setTitle(HtmlUtils.htmlUnescape(exhibitionDetail.getTitle()));
        exhibitionDetail.setSubTitle(HtmlUtils.htmlUnescape(exhibitionDetail.getSubTitle()));

        exhibitionDetailRepository.save(exhibitionDetail);
    }

    public List<Exhibition> findExhibitionListByDate(String keyword, String date, Double latitude, Double longitude, int distance) {
        return exhibitionRepository.findExhibitionListByDate(keyword, date, latitude, longitude, distance);
    }

    public Exhibition findExhibitionById(String id) {
        return exhibitionRepository.findById(Long.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 exhibition id : " + id));
    }
    public ExhibitionDetail findExhibitionDetail(String id) {
        return exhibitionDetailRepository.findById(Long.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 exhibition id : " + id));
    }
}
