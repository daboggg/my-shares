package ru.zinin.myshares.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.service.UserService;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    @CrossOrigin(methods = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping(path = "/login")
    @CrossOrigin(methods = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        return userService.login(user);
    }
}
