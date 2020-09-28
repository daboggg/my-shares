package ru.zinin.myshares.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.zinin.myshares.component.TokenFactory;

import java.net.URL;
import java.util.Map;

@Service
public class ChartService {

    @Value("${ameritrade.key}")
    private String key;

    private final TokenFactory tokenFactory;
    private final RestTemplate restTemplate;

    public ChartService(TokenFactory tokenFactory, RestTemplate restTemplate) {
        this.tokenFactory = tokenFactory;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> getDataForChart(String ticker) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            String url = "https://api.tdameritrade.com/v1/marketdata/" + ticker + "/pricehistory?apikey=" + key + "&periodType=year&period=1&frequencyType=daily&frequency=1";
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
