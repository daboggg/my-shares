package ru.zinin.myshares.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.zinin.myshares.component.TokenFactory;
import ru.zinin.myshares.model.Transaction;
import ru.zinin.myshares.model.TransactionDto;
import ru.zinin.myshares.repo.TransactionRepo;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
@PropertySource("classpath:key.properties")
public class TransactionService {

    @Value("${ameritrade.key}")
    private String key;

    private final TransactionRepo transactionRepo;
    private final TokenFactory tokenFactory;
    private final RestTemplate restTemplate;

    public TransactionService(TransactionRepo transactionRepo, TokenFactory tokenFactory, RestTemplate restTemplate) {
        this.transactionRepo = transactionRepo;
        this.tokenFactory = tokenFactory;
        this.restTemplate = restTemplate;
    }

//    @PostConstruct
//    public void add() {
//        List<Transaction> transactions = new ArrayList<>() {{
//            add(new Transaction("MSFT", true, 5.65, 8, 54466656555588l, 1L, "kjhjkjh"));
//            add(new Transaction("T", false, 48.96, 25, 544655546565688l, 1L, "gfdgfd"));
//            add(new Transaction("MO", false, 57.65, 3, 5446665656554545688l, 1L, "lk454k45"));
//        }};
//
//        transactionRepo.saveAll(transactions);
//    }

    public ResponseEntity<?> getTransactions() throws IOException {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            List<Transaction> transactionsFromDb = transactionRepo.getAllByUserId(tokenFactory.getUserId());
            if (transactionsFromDb.size() == 0) return ResponseEntity.ok(transactionsFromDb);
            Map info = addInfo(transactionsFromDb);
            List sumInfo = new ArrayList();
            sumInfo.add(transactionsFromDb);
            sumInfo.add(info);
            return ResponseEntity.ok(sumInfo);
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public Map addInfo(List<Transaction> transactions) throws IOException {
        Set<String> setTickers = new HashSet<>();
        transactions.forEach(i -> setTickers.add(i.getTicker()));

        StringJoiner joiner = new StringJoiner(",");
        setTickers.forEach(t -> joiner.add(t));
        String tickers = joiner.toString();

        String url = "https://api.tdameritrade.com/v1/marketdata/quotes?apikey=" + key + "&symbol=" + tickers;
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(new URL(url), Map.class);
    }

    public ResponseEntity<?> addTransaction(Transaction transaction) throws IOException {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            transaction.setUserId(tokenFactory.getUserId());
            return ResponseEntity.ok(transactionRepo.save(transaction));
        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> editTransaction(Transaction transaction) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            Transaction transactionFromDB = transactionRepo.getById(transaction.getId());
            if (tokenFactory.getUserId() == transactionFromDB.getUserId()) {
                transaction.setUserId(tokenFactory.getUserId());
                return ResponseEntity.ok(transactionRepo.save(transaction));
            } else return new ResponseEntity<>(
                    "editing is prohibited",
                    HttpStatus.FORBIDDEN
            );

        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public ResponseEntity<?> deleteTransaction(Long id) {
        if (tokenFactory.isValidToken()) {
            tokenFactory.updateTimeValidityToken();
            Transaction transactionFromDB = transactionRepo.getById(id);
            if (tokenFactory.getUserId() == transactionFromDB.getUserId()) {
                transactionRepo.delete(transactionFromDB);
                return ResponseEntity.ok("OK");
            } else return new ResponseEntity<>(
                    "deletion is prohibited",
                    HttpStatus.FORBIDDEN
            );

        } else {
            return new ResponseEntity<>(
                    "invalid token",
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
