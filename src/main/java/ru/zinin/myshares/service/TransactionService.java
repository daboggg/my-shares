package ru.zinin.myshares.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.Transaction;
import ru.zinin.myshares.repo.TransactionRepo;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final TokenFactory tokenFactory;

    public TransactionService(TransactionRepo transactionRepo, TokenFactory tokenFactory) {
        this.transactionRepo = transactionRepo;
        this.tokenFactory = tokenFactory;
    }

    @PostConstruct
    public void add() {
        List<Transaction> transactions = new ArrayList<>(){{
            add(new Transaction("MSFT", true, 5.65, 8, 54466656555588l,1L));
            add(new Transaction("T", false, 48.96, 25, 544655546565688l,1L));
            add(new Transaction("MO", false, 57.65, 3, 5446665656554545688l,1L));
            add(new Transaction("RSNT", true, 585.65, 82, 5455454565688l,1L));
        }};

        transactionRepo.saveAll(transactions);
    }

    public ResponseEntity<?> getTransactions() {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            return ResponseEntity.ok(transactionRepo.findAll());
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
