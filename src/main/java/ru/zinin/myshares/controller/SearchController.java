package ru.zinin.myshares.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.service.SearchService;

import java.io.IOException;

@RestController
@RequestMapping(path = "/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(path = "/instrument")
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> searchInstrument(
            @RequestParam String searchString,
            @RequestParam boolean searchByName
            )  {
        return searchService.searchInstrument(searchString, searchByName);
    }
}
