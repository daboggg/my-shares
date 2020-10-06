package ru.zinin.myshares.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.Alert;
import ru.zinin.myshares.model.Transaction;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.repo.AlertRepo;
import ru.zinin.myshares.repo.UserPepo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AlertService {

    @Value("${ameritrade.key}")
    private String key;

    private final TokenFactory tokenFactory;
    private final UserPepo userPepo;
    private final AlertRepo alertRepo;

    public AlertService(TokenFactory tokenFactory, UserPepo userPepo, AlertRepo alertRepo) {
        this.tokenFactory = tokenFactory;
        this.userPepo = userPepo;
        this.alertRepo = alertRepo;
    }

    public ResponseEntity<?> addAlert(Alert alert) throws IOException {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            User user = userPepo.getOne(tokenFactory.getUserId());
            alert.setUserId(user.getId());
            alert.setEmail(user.getEmail());

            String url = "https://api.tdameritrade.com/v1/marketdata/quotes?apikey=" + key + "&symbol=" + alert.getTicker();
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(new URL(url), Map.class);
            Map tickerMap = (Map) map.get(alert.getTicker());
            double ctrlValue = (double) tickerMap.get("lastPrice");

            alert.setCtrlValue(ctrlValue);

            return ResponseEntity.ok(alertRepo.save(alert));
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> getAlerts() {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            List<Alert> alerts = alertRepo.getAllByUserId(tokenFactory.getUserId());
            return ResponseEntity.ok(alerts);
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> deleteAlert(Long id) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            Alert alert = alertRepo.getById(id);
            System.out.println("ALERT:   "+alert);
            if (alert!= null && alert.getUserId() == tokenFactory.getUserId()) {
                alertRepo.delete(alert);
                return ResponseEntity.ok("ALERT DELETED");
            } else {
                return new ResponseEntity<>(
                        "wrong alertId",
                        HttpStatus.FORBIDDEN
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
