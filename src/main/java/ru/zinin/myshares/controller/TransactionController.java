package ru.zinin.myshares.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.model.Transaction;
import ru.zinin.myshares.service.TransactionService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> getTransactions() {
        return transactionService.getTransactions();
    }

}
