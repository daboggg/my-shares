package ru.zinin.myshares.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.service.ChartService;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/chart")
public class ChartController {

    private final ChartService chartService;

    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @GetMapping
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> getDataForChart(@RequestParam String ticker)  {
        return chartService.getDataForChart(ticker);
    }
}
