package me.croco.onulmohaji.festival.controller;

import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.dto.PlaceDetailFindResponse;
import me.croco.onulmohaji.dto.PlaceListFindResponse;
import me.croco.onulmohaji.festival.domain.Festival;
import me.croco.onulmohaji.festival.dto.FestivalDetailFindResponse;
import me.croco.onulmohaji.festival.service.FestivalService;
import me.croco.onulmohaji.localcode.domain.Localcode;
import me.croco.onulmohaji.localcode.service.LocalcodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FestivalController {

    private final FestivalService festivalService;
    private final LocalcodeService localcodeService;


    @GetMapping("/festival/new")
    public ResponseEntity<List<Festival>> getNewFestivalList() {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(festivalService.saveNewFestivalList());
    }

//    // 데이터가 정확하지 않아 사용하지 않음
//    @GetMapping("/festival/new/calendar")
//    public ResponseEntity<List<Festival>> getNewFestivalListByCalendar() throws Exception{
//        List<Festival> festivalList = festivalService.saveNewFestivalListByCalendar();
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(festivalList);
//    }

    @GetMapping("/api/festival/list")
    public ResponseEntity<List<PlaceListFindResponse>> findFestivalListByDate(@RequestParam String date, @RequestParam Double latitude, @RequestParam Double longitude, @RequestParam int distance, @RequestParam(required = false) String keyword) {
        List<Festival> festivalList = festivalService.findFestivalListByDate(keyword, date, latitude, longitude, distance);

        List<PlaceListFindResponse> festivalListFindResponseList = festivalList.stream().map(festival -> new PlaceListFindResponse(festival, latitude, longitude)).toList();

        return ResponseEntity.ok()
                .body(festivalListFindResponseList);

    }

    @GetMapping("/api/festival/{id}")
    public ResponseEntity<FestivalDetailFindResponse> findFestivalDetail(@PathVariable String id) {
        Festival festival = festivalService.findFestivalById(id);

        return ResponseEntity.ok()
                .body(new FestivalDetailFindResponse(festival));
    }

}
