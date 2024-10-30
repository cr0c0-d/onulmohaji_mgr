package me.croco.onulmohaji.api;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.RequiredArgsConstructor;
import me.croco.onulmohaji.api.dto.NaverTokenResponse;
import me.croco.onulmohaji.config.WebClientConfig;
import me.croco.onulmohaji.dto.NaverLocalFindRequest;
import me.croco.onulmohaji.dto.NaverLocalFindResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class NaverService {

    @Value("${spring.security.oauth2.client.registration.naver.client_id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    private static final String NAVER_API_BASE_URL = "https://openapi.naver.com";

    private static final String SEARCH_LOCAL_URI = "/v1/search/local.json";
    private static final String MAKE_SHORT_URL_API_URL = "/v1/util/shorturl.xml";

    @Value("${onulmohaji.croco.front}")
    private String origin;


    public NaverLocalFindResponse searchLocal(NaverLocalFindRequest request) {
        WebClient webClient = getNaverApiWebClient();

        String uri = SEARCH_LOCAL_URI + "?query=" + request.getQuery();

        String responseBody = webClient.get()
                                .uri(uri)
                                .retrieve()
                                .bodyToMono(String.class)
                                .block();

        ObjectMapper mapper = JsonMapper.builder()
                                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                                .build();
        NaverLocalFindResponse response = null;

        try {
            response = mapper.readValue(responseBody, NaverLocalFindResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public String getNaverLoginRequestUrl(String state) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", naverClientId);
        params.put("response_type", "code");
        params.put("redirect_uri", origin+"/login/auth/naver/callback");
        params.put("state", state);

        StringBuilder builder = new StringBuilder();
        builder.append("https://nid.naver.com/oauth2.0/authorize");
        builder.append("?");
        params.keySet().forEach(key -> {
            builder.append(key);
            builder.append("=");
            builder.append(params.get(key));
            builder.append("&");
        });
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();

    }

    public NaverTokenResponse getAccessTokenNaver(String code, String state) {
        WebClient webClient = WebClient.builder()
                                .baseUrl("https://nid.naver.com")
                                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .defaultHeader("X-Naver-Client-Id", naverClientId)
                                .defaultHeader("X-Naver-Client-Secret", naverClientSecret)
                                .build();

        String responseBody = webClient.get()
                            .uri(uriBuilder ->
                                uriBuilder.path("/oauth2.0/token")
                                    .queryParam("grant_type", "authorization_code")
                                    .queryParam("client_id", naverClientId)
                                    .queryParam("client_secret", naverClientSecret)
                                    .queryParam("code", code)
                                    .queryParam("state", state)
                                    .build()
                            )
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

        ObjectMapper mapper = JsonMapper.builder()
                .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
                .build();

        NaverTokenResponse response = null;

        try {
            response = mapper.readValue(responseBody, NaverTokenResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    private WebClient getNaverApiWebClient() {
        return WebClient.builder()
                .baseUrl(NAVER_API_BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Naver-Client-Id", naverClientId)
                .defaultHeader("X-Naver-Client-Secret", naverClientSecret)
                .build();
    }
}
