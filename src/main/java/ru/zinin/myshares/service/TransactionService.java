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

    @PostConstruct
    public void add() {
        List<Transaction> transactions = new ArrayList<>(){{
            add(new Transaction("MSFT", true, 5.65, 8, 54466656555588l,1L,"kjhjkjh"));
            add(new Transaction("T", false, 48.96, 25, 544655546565688l,1L,"gfdgfd"));
            add(new Transaction("MO", false, 57.65, 3, 5446665656554545688l,1L,"lk454k45"));
        }};

        transactionRepo.saveAll(transactions);
    }

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
        setTickers.forEach(t->joiner.add(t));
        String tickers = joiner.toString();

        String url = "https://api.tdameritrade.com/v1/marketdata/quotes?apikey=" + key + "&symbol=" + tickers;
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(new URL(url), Map.class);
    }
    /*public List<TransactionDto> toTransactionDto(List<Transaction> transactions) throws IOException {
        // создаеме строку с разделитем "," из тикеров
        StringJoiner joiner = new StringJoiner(",");
        transactions.forEach(t->joiner.add(t.getTicker()));
        String tickers = joiner.toString();

        String url = "https://api.tdameritrade.com/v1/marketdata/quotes?apikey=" + key + "&symbol=" + tickers;
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(new URL(url), Map.class);

        List<TransactionDto> transactionDtos = new ArrayList<>();

        for (Transaction transaction : transactions) {
            Map tmp = (Map) map.get(transaction.getTicker());
            TransactionDto transactionDto = new TransactionDto(
                    transaction.getId(),
                    transaction.getTicker(),
                    transaction.isDirectionOfTransaction(),
                    transaction.getPrice(),
                    transaction.getNumberOfShares(),
                    transaction.getTransactionDate(),
                    (String) tmp.get("cusip"),
                    (String) tmp.get("description"),
                    (double) tmp.get("lastPrice"),
                    transaction.getNote()
            );
            transactionDtos.add(transactionDto);
        }

        return transactionDtos;
        // Доделать
    }*/

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
}
