package ru.zinin.myshares.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.zinin.myshares.component.EmailServiceImpl;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.service.TestService;

@RestController
@PropertySource("classpath:key.properties")
public class TestController {

    @Value("${ameritrade.key}")
    private String key;

    private final TestService testService;
    private final TokenFactory tokenFactory;
    private final RestTemplate restTemplate;
    private final EmailServiceImpl emailService;

    public TestController(TestService testService, TokenFactory tokenFactory, RestTemplate restTemplate, EmailServiceImpl emailService) {
        this.testService = testService;
        this.tokenFactory = tokenFactory;
        this.restTemplate = restTemplate;
        this.emailService = emailService;
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
    public ResponseEntity<?> test() {
        emailService.sendSimpleMessage("a","b","c");
        return ResponseEntity.ok("OK");
    }
}
