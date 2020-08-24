package ru.zinin.myshares.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.service.TestService;

@RestController
public class TestController {

    private final TestService testService;
    private final TokenFactory tokenFactory;

    public TestController(TestService testService, TokenFactory tokenFactory) {
        this.testService = testService;
        this.tokenFactory = tokenFactory;
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
        return ResponseEntity.ok(tokenFactory.getTokens());
    }
}
