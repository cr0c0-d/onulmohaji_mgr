package me.croco.onulmohaji.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.croco.onulmohaji.api.dto.KoreanFestivalListFindResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KoreanFestivalService {
    private static final String KOREAN_VISIT_KOREA_BASE_URL = "https://korean.visitkorea.or.kr";
    private static final String KOREAN_VISIT_FIND_FESTIVAL_LIST = "/kfes/list/selectWntyFstvlList.do";

    private static final String KOREAN_VISIT_FIND_FESTIVAL_LIST_CALENDAR = "/kfes/list/festivalCalendarList.do";

    public List<KoreanFestivalListFindResponse> getFestivalList() {
        LocalDate today = LocalDate.now();

        List<KoreanFestivalListFindResponse> festivalList = new ArrayList<>();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("searchType", "A");
        params.add("searchDate", today.format(DateTimeFormatter.ofPattern("MM")));
        params.add("searchArea", "");
        params.add("searchCate", "");
        params.add("locationx", "undefined");
        params.add("locationy", "undefined");
        params.add("filterExcluded", "true");

        int startIdx = 0;

        while (true) {
            params.set("startIdx", String.valueOf(startIdx));

            String response = getWebClient().post()
                    .uri(KOREAN_VISIT_FIND_FESTIVAL_LIST)
                    .bodyValue(params)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode rootNode = mapper.readTree(response);
                JsonNode dataNode = rootNode.path("resultList");
                if (!dataNode.isMissingNode()) { // 'resultList' 필드가 존재하는지 확인
                    List<KoreanFestivalListFindResponse> newFestivals = mapper.convertValue(dataNode, new com.fasterxml.jackson.core.type.TypeReference<List<KoreanFestivalListFindResponse>>() {
                    });
                    if(newFestivals.isEmpty()) {
                        break;
                    } else {
                        festivalList.addAll(newFestivals);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            startIdx += 12;
        }

        return festivalList;
    }

    public List<KoreanFestivalListFindResponse> getFestivalListByCalendar() throws Exception {
        // 이번달 1일부터 시작
        LocalDate date = LocalDate.now().withDayOfMonth(1);

        int thisMonth = date.getMonthValue();

        List<KoreanFestivalListFindResponse> festivalList = new ArrayList<>();

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
        builder.uri(new URI(KOREAN_VISIT_FIND_FESTIVAL_LIST_CALENDAR))
                .queryParam("year", date.getYear())
                .queryParam("month", date.getMonthValue())
                .queryParam("day", date.getDayOfMonth())
                .queryParam("page", 0)
                .queryParam("offset", 100);


        while (true) {


            String response = getWebClient().get()
                    .uri(builder.toUriString())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode rootNode = mapper.readTree(response);
                JsonNode dataNode = rootNode.path("dataList").path("items");
                if (!dataNode.isMissingNode()) { // 존재하는지 확인
                    List<KoreanFestivalListFindResponse> newFestivals = mapper.convertValue(dataNode, new com.fasterxml.jackson.core.type.TypeReference<List<KoreanFestivalListFindResponse>>() {
                    });
                    if(newFestivals.isEmpty()) {
                        break;
                    } else {
                        festivalList.addAll(newFestivals);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            date = date.plusDays(1);
            if(date.getMonthValue() == thisMonth+2) {
                break;
            } else {
                builder.replaceQueryParam("year", date.getYear())
                        .replaceQueryParam("month", date.getMonthValue())
                        .replaceQueryParam("day", date.getDayOfMonth());
            }
        }
        return festivalList;
    }

    private WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(KOREAN_VISIT_KOREA_BASE_URL)
                .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
    }
}
