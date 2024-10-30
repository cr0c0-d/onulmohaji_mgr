package me.croco.onulmohaji.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.exhibition.domain.Exhibition;
import me.croco.onulmohaji.exhibition.domain.ExhibitionDetail;
import me.croco.onulmohaji.exhibition.service.ExhibitionService;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicExhibitionService {

    private static final String BASE_URL = "http://www.culture.go.kr";

    // 공연 정보 조회
    private static final String SEARCH_EXHIBITION_URL = "/openapi/rest/publicperformancedisplays/period";

    // 공연 상세 정보 조회
    private static final String SEARCH_EXHIBITION_DETAIL_URL = "/openapi/rest/publicperformancedisplays/d/";    // ?seq={공연/전시번호}

    private final ExhibitionService exhibitionService;

    @Value("${apiKey.exhibitionSearch}")
    private String apiKey;

    private WebClient getWebClient() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        return WebClient.builder()
                .baseUrl(BASE_URL)
                .uriBuilderFactory(factory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // 오늘부터 한달간의 전시회/공연 정보 조회하여 저장
    public void getNewExhibitions() {
        LocalDate today = LocalDate.now();
        LocalDate afterOneMonth = today.plusDays(30);

        WebClient webClient = getWebClient();
        String response = webClient.get()
                                .uri(uriBuilder -> uriBuilder.path(SEARCH_EXHIBITION_URL)
                                        .queryParam("from", URLEncoder.encode(today.format(DateTimeFormatter.ofPattern("yyyyMMdd")), StandardCharsets.UTF_8))
                                        .queryParam("to", URLEncoder.encode(afterOneMonth.format(DateTimeFormatter.ofPattern("yyyyMMdd")), StandardCharsets.UTF_8))
                                        .queryParam("cPage", URLEncoder.encode("1", StandardCharsets.UTF_8))
                                        .queryParam("rows", URLEncoder.encode("100", StandardCharsets.UTF_8))
//                                        .queryParam("place", URLEncoder.encode("", StandardCharsets.UTF_8))
//                                        .queryParam("gpsxfrom", URLEncoder.encode("129.1013129", StandardCharsets.UTF_8))
//                                        .queryParam("gpsyfrom", URLEncoder.encode("35.1416412", StandardCharsets.UTF_8))
//                                        .queryParam("gpsxto", URLEncoder.encode("129.1013129", StandardCharsets.UTF_8))
//                                        .queryParam("gpsyto", URLEncoder.encode("35.1416412", StandardCharsets.UTF_8))
//                                        .queryParam("keyword", URLEncoder.encode(request.getKeyword(), StandardCharsets.UTF_8))
                                        .queryParam("sortStdr", URLEncoder.encode("1", StandardCharsets.UTF_8))
                                        .queryParam("serviceKey", apiKey)
                                        .build()
                                )
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

        ObjectMapper mapper = new ObjectMapper();

        try {
            JSONObject jsonObject = XML.toJSONObject(response);

            // JSONObject를 JSON 문자열로 변환
            String jsonStr = jsonObject.toString();

            JsonNode rootNode = mapper.readTree(jsonStr);
            JsonNode bodyNode = rootNode.path("response").path("msgBody").path("perforList");

            if (!bodyNode.isMissingNode()) { // 'msgBody' 필드가 존재하는지 확인
                List<Exhibition> exhibitions = mapper.convertValue(bodyNode, new com.fasterxml.jackson.core.type.TypeReference<List<Exhibition>>() {
                });

                exhibitions = exhibitionService.saveExhibitionFromSearchResult(exhibitions);
                getNewExhibitionDetails(exhibitions);   // 세부정보 조회하여 저장
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNewExhibitionDetails(List<Exhibition> exhibitionList) {
        WebClient webClient = getWebClient();
        ObjectMapper mapper = new ObjectMapper();

        exhibitionList.forEach(exhibition -> {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path(SEARCH_EXHIBITION_DETAIL_URL)
                            .queryParam("serviceKey", apiKey)
                            .queryParam("seq", exhibition.getSeq())
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            try {
                JSONObject jsonObject = XML.toJSONObject(response);

                // JSONObject를 JSON 문자열로 변환
                String jsonStr = jsonObject.toString();

                JsonNode rootNode = mapper.readTree(jsonStr);
                JsonNode bodyNode = rootNode.path("response").path("msgBody").path("perforInfo");

                if (!bodyNode.isMissingNode()) { // 'msgBody' 필드가 존재하는지 확인
                    ExhibitionDetail exhibitionDetail = mapper.convertValue(bodyNode, ExhibitionDetail.class);
                    exhibitionService.saveExhibitionDetailFromSearchResult(exhibitionDetail);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
