package ru.zinin.myshares.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.zinin.myshares.component.TokenFactory;

import java.net.URL;
import java.util.Map;

@Service
@PropertySource("classpath:key.properties")
public class SearchService {

    @Value("${ameritrade.key}")
    private String key;

    private final TokenFactory tokenFactory;
    private final RestTemplate restTemplate;

    public SearchService(TokenFactory tokenFactory, RestTemplate restTemplate) {
        this.tokenFactory = tokenFactory;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> searchInstrument(String searchString, boolean searchByName) {
        String searchType;

        if (searchByName) searchType = "desc-search";
        else searchType = "symbol-search";

        String url = "https://api.tdameritrade.com/v1/instruments?apikey=" + key + "&symbol=" + searchString + "&projection=" + searchType;

        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            ////////
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                Map map = objectMapper.readValue(new URL(url), Map.class);
                return ResponseEntity.ok(map);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        e,
                        HttpStatus.NOT_FOUND
                );
            }
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
