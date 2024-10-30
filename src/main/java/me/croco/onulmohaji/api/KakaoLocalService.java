package me.croco.onulmohaji.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.dto.KakaoLocalListFindResponse;
import me.croco.onulmohaji.facility.domain.Facility;
import me.croco.onulmohaji.facility.service.FacilityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class KakaoLocalService {

    // api 문서
    // https://developers.kakao.com/docs/latest/ko/local/dev-guide#search-by-keyword

    private static final String KAKAO_LOCAL_BASE_URL = "https://dapi.kakao.com";
    private static final String KAKAO_LOCAL_GET_ADDRESS = "/v2/local/search/address";
    private static final String KAKAO_LOCAL_SEARCH_BY_CATEGORY = "/v2/local/search/category";

    private static final String KAKAO_LOCAL_SEARCH_DETAIL = "https://place.map.kakao.com/main/v/";  // localId

    private static final String KAKAO_LOCAL_TRANSCOORD = "/v2/local/geo/transcoord.json";

    private static final String KAKAO_LOCAL_SEARCH_KEYWORD = "/v2/local/search/keyword";

    @Value("${spring.security.oauth2.client.registration.kakao.client_id}")
    private String apiKey;

    public List<Double> getAddress() {
        return null;
    }

    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(KAKAO_LOCAL_BASE_URL)
                .defaultHeader("Authorization", "KakaoAK " + apiKey)
                .build();
    }

    // 카테고리 코드
//    MT1	대형마트
//    CS2	편의점
//    PS3	어린이집, 유치원
//    SC4	학교
//    AC5	학원
//    PK6	주차장
//    OL7	주유소, 충전소
//    SW8	지하철역
//    BK9	은행
//    CT1	문화시설
//    AG2	중개업소
//    PO3	공공기관
//    AT4	관광명소
//    AD5	숙박
//    FD6	음식점
//    CE7	카페
//    HP8	병원
//    PM9	약국

    // 지하철역
    // PK6 주차장
    // CT1 문화시설
    // AT4 관광명소
    // FD6 음식점
    // CE7 카페

    public List<Facility> getLocalListByCategory(String keyword, String categoryId, Double latitude, Double longitude) {
        WebClient webClient = getWebClient();

        String response = webClient.get()
                            .uri(uriBuilder ->
                                uriBuilder.path( keyword == null ? KAKAO_LOCAL_SEARCH_BY_CATEGORY : KAKAO_LOCAL_SEARCH_KEYWORD)
                                        .queryParam(keyword == null ? "category_group_code" : "query", keyword == null ? categoryId : keyword)
                                        .queryParam("x", longitude)
                                        .queryParam("y", latitude)
                                        .queryParam("radius", 20000)
                                        .queryParam("sort", "accuracy")
                                        .build()

                            )
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode dataNode = rootNode.path("documents");
            if (!dataNode.isMissingNode()) { // 'documents' 필드가 존재하는지 확인
                List<KakaoLocalListFindResponse> facilityList = mapper.convertValue(dataNode, new com.fasterxml.jackson.core.type.TypeReference<List<KakaoLocalListFindResponse>>() {
                });

                return facilityList.stream().map(Facility::new).map(this::getFacilityDetail).toList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Facility의 상세정보 조회
    public Facility getFacilityDetail(Facility facility) {
        Map<String, String> facilityDetail = new HashMap<>();

        String response = WebClient.create(KAKAO_LOCAL_SEARCH_DETAIL)
                .get()
                .uri(String.valueOf(facility.getId()))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode dataNode = rootNode.path("basicInfo");
            if (!dataNode.isMissingNode()) { // 'basicInfo' 필드가 존재하는지 확인

                facility.setThumbnail(mapper.convertValue(dataNode.path("mainphotourl"), String.class));
                facility.setScoresum(mapper.convertValue(dataNode.path("feedback").path("scoresum"), Integer.class));
                facility.setScorecnt(mapper.convertValue(dataNode.path("feedback").path("scorecnt"), Integer.class));

                StringBuilder builder = new StringBuilder();
                List<String> tagList = mapper.convertValue(dataNode.path("tags"), new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
                if(tagList != null) {
                    tagList.forEach(tag->{
                        builder.append(tag);
                        builder.append(",");
                    });
                }
                facility.setTags(builder.toString());

                facility.setWpointx(mapper.convertValue(dataNode.path("wpointx"), Long.class));
                facility.setWpointy(mapper.convertValue(dataNode.path("wpointy"), Long.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return facility;
    }

    // 좌표계 변환
    public List<Long> getTranscoord(Double longitude, Double latitude) {
        List<Long> list = new ArrayList<>();

        WebClient webClient = getWebClient();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(KAKAO_LOCAL_TRANSCOORD)
                        .queryParam("x", longitude)
                        .queryParam("y", latitude)
                        .queryParam("output_coord","WCONGNAMUL")
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode dataNode = rootNode.path("documents").get(0);
            list.add((mapper.convertValue(dataNode.path("x"), Double.class).longValue()));
            list.add((mapper.convertValue(dataNode.path("y"), Double.class).longValue()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // 주소 -> 좌표계 검색
    public List<Double> getAddressInfo(String address) {
        List<Double> list = new ArrayList<>();

        WebClient webClient = getWebClient();
        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(KAKAO_LOCAL_GET_ADDRESS)
                        .queryParam("query", address)
                        .queryParam("page", 1)
                        .queryParam("size", 1)
                        .build()
                )
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(response);
            JsonNode dataNode = rootNode.path("documents");
            if(!dataNode.isMissingNode() && !dataNode.isEmpty()) {
                list.add(mapper.convertValue(dataNode.get(0).path("address").path("x"), Double.class));
                list.add(mapper.convertValue(dataNode.get(0).path("address").path("y"), Double.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


}
