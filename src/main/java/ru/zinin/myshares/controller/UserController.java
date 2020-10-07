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

    @PutMapping(path = "/changeUsername")
    @CrossOrigin(methods = RequestMethod.PUT)
    public ResponseEntity<?> changeUsername(@RequestBody String newUsername) {
        return userService.changeUsername(newUsername);
    }

    @GetMapping(path = "/profile")
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> getProfile() {
        return userService.getProfile();
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
