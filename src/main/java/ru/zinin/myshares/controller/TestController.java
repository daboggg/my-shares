package ru.zinin.myshares.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.service.TestService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@RestController
@PropertySource("classpath:key.properties")
public class TestController {

    @Value("${ameritrade.key}")
    private String key;

    private final TestService testService;
    private final TokenFactory tokenFactory;
    private final RestTemplate restTemplate;

    public TestController(TestService testService, TokenFactory tokenFactory, RestTemplate restTemplate) {
        this.testService = testService;
        this.tokenFactory = tokenFactory;
        this.restTemplate = restTemplate;
    }

    @PostMapping(path = "/register")
//    @CrossOrigin(methods = RequestMethod.POST)
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ResponseEntity<?> register(@RequestBody User user) throws Exception {
        if (false) {
            return new ResponseEntity(new User(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("POPOPO");
    }

    @GetMapping(path = "/test")
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> test() throws IOException {
        String url = "https://api.tdameritrade.com/v1/marketdata/quotes?apikey=" + key + "&symbol=O";
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(new URL(url), Map.class);
        Map asd = (Map) map.get("O");
        System.out.println(asd.get("description"));
        return ResponseEntity.ok(map);
    }
}
