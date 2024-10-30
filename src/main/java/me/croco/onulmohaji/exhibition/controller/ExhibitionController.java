package me.croco.onulmohaji.exhibition.controller;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.PublicExhibitionService;
import me.croco.onulmohaji.dto.PlaceDetailFindResponse;
import me.croco.onulmohaji.exhibition.domain.Exhibition;
import me.croco.onulmohaji.dto.PlaceListFindResponse;
import me.croco.onulmohaji.exhibition.domain.ExhibitionDetail;
import me.croco.onulmohaji.exhibition.dto.ExhibitionDetailFindResponse;
import me.croco.onulmohaji.exhibition.service.ExhibitionService;
import me.croco.onulmohaji.localcode.domain.Localcode;
import me.croco.onulmohaji.localcode.service.LocalcodeService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExhibitionController {

    private final PublicExhibitionService publicExhibitionService;
    private final ExhibitionService exhibitionService;
    private final LocalcodeService localcodeService;

    @GetMapping("/api/exhibition/new")
    public void searchLocal() {
        publicExhibitionService.getNewExhibitions();
    }

    @GetMapping("/api/exhibition/list")
    public ResponseEntity<List<PlaceListFindResponse>> findExhibitionListByDate(@RequestParam String date, @RequestParam Double latitude, @RequestParam Double longitude, @RequestParam int distance, @RequestParam(required = false) String keyword) {

        List<Exhibition> exhibitionList = exhibitionService.findExhibitionListByDate(keyword, date, latitude, longitude, distance);

        List<PlaceListFindResponse> popupstoreListFindResponseList = exhibitionList.stream().map(exhibition -> new PlaceListFindResponse(exhibition, latitude, longitude)).toList();

        return ResponseEntity.ok()
                .body(popupstoreListFindResponseList);

    }

    @GetMapping("/api/exhibition/{id}")
    public ResponseEntity<ExhibitionDetailFindResponse> findExhibitionDetail(@PathVariable String id) {
        Exhibition exhibition = exhibitionService.findExhibitionById(id);
        ExhibitionDetail exhibitionDetail = exhibitionService.findExhibitionDetail(id);

        return ResponseEntity.ok()
                .body(new ExhibitionDetailFindResponse(exhibition, exhibitionDetail));
    }

}
