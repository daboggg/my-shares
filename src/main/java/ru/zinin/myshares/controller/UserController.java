package ru.zinin.myshares.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.model.User;
import ru.zinin.myshares.service.UserService;

@RestController
@RequestMapping(path = "/api/user")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping(path = "/changePassword")
    @CrossOrigin(methods = RequestMethod.PUT)
    public ResponseEntity<?> changePassword(@RequestBody String newPassword) {
        return userService.changePassword(newPassword);
    }

    @PostMapping(path = "/checkOldPassword")
    @CrossOrigin(methods = RequestMethod.POST)
    public ResponseEntity<?> checkOldPassword(@RequestBody String oldPassword) {
        return userService.checkOldPassword(oldPassword);
    }

    @PutMapping(path = "/changeUsername")
    @CrossOrigin(methods = RequestMethod.PUT)
    public ResponseEntity<?> changeUsername(@RequestBody String newUsername) {
        return userService.changeUsername(newUsername);
    }

    @PutMapping(path = "/changeEmail")
    @CrossOrigin(methods = RequestMethod.PUT)
    public ResponseEntity<?> changeEmail(@RequestBody String newEmail) {
        return userService.changeEmail(newEmail);
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
