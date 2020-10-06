package ru.zinin.myshares.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.model.Alert;
import ru.zinin.myshares.service.AlertService;

import java.io.IOException;

@RestController
@RequestMapping(path = "/api/alert")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    @CrossOrigin(methods = RequestMethod.POST)
    public ResponseEntity<?> addAlert(@RequestBody Alert alert) throws IOException {
        return alertService.addAlert(alert);
    }

    @GetMapping
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> getAlarts() {
        return alertService.getAlerts();
    }

    @DeleteMapping
    @CrossOrigin(methods = RequestMethod.DELETE)
    public ResponseEntity<?> deleteAlert(@RequestParam Long id) {
        return alertService.deleteAlert(id);
    }
}
