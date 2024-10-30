package me.croco.onulmohaji.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.dto.PopplyPopupstoreFindResponse;
import me.croco.onulmohaji.popupstore.service.PopupstoreService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopplyService {

    private static final String POPPLY_BASE_URL = "https://api.popply.co.kr";
    private static final String POPPLY_FIND_LIST_URL = "/api/store/";  // ?startDate=2024-05-01&endDate=2024-05-31
    private static final String POPPLY_FIND_STORE_URL = "/api/store/";   // 1454 (store Id)

    private final PopupstoreService popupstoreService;


    public void getPopupstoreInfo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);
        List<PopplyPopupstoreFindResponse> list = new ArrayList<>();

        WebClient webClient = WebClient.builder()
                .exchangeStrategies(builder -> builder.codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(-1)))
                .baseUrl(POPPLY_BASE_URL)
                .build();

        while(true) {

            LocalDate queryDate = today;
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder.path(POPPLY_FIND_LIST_URL)
                            .queryParam("startDate", dateFormatter.format(queryDate))
                            .queryParam("endDate", dateFormatter.format(queryDate))
                            .build()
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            try {
                JsonNode rootNode = mapper.readTree(response);
                JsonNode dataNode = rootNode.path("data");
                if (!dataNode.isMissingNode()) { // 'data' 필드가 존재하는지 확인
                    List<PopplyPopupstoreFindResponse> stores = mapper.convertValue(dataNode, new com.fasterxml.jackson.core.type.TypeReference<List<PopplyPopupstoreFindResponse>>() {
                    });
                    List<PopplyPopupstoreFindResponse> filtered = stores.stream().filter(responseStore -> list.stream().noneMatch(store -> store.getStoreId().equals(responseStore.getStoreId()))).toList();
                    list.addAll(filtered);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(queryDate.isEqual(oneMonthLater)) {
                break;
            } else {
                today = today.plusDays(1);
            }
        }
        popupstoreService.savePopupstoreInfo(list);

    }
}
