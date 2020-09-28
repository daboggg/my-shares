package ru.zinin.myshares.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.zinin.myshares.model.Transaction;
import ru.zinin.myshares.service.TransactionService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    @CrossOrigin(methods = RequestMethod.GET)
    public ResponseEntity<?> getTransactions() throws IOException {
        return transactionService.getTransactions();
    }
    @PostMapping
    @CrossOrigin(methods = RequestMethod.POST)
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) throws IOException {
        return transactionService.addTransaction(transaction);
    }

    @PutMapping
    @CrossOrigin(methods = RequestMethod.PUT)
    public ResponseEntity<?> editTransaction(@RequestBody Transaction transaction) {
        return transactionService.editTransaction(transaction);
    }

    @DeleteMapping
    @CrossOrigin(methods = RequestMethod.DELETE)
    public ResponseEntity<?> deleteTransaction(@RequestParam Long id) {
        return transactionService.deleteTransaction(id);
    }
}
