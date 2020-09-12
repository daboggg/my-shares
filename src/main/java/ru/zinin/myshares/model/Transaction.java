package ru.zinin.myshares.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "transac")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String ticker;
    private boolean directionOfTransaction;
    private double price;
    private int numberOfShares;
    private long transactionDate;
    private Long userId;
    @Column(length = 10000)
    private String note;

    public Transaction(String ticker, boolean directionOfTransaction, double price, int numberOfShares, long transactionDate, Long userId, String note) {
        this.ticker = ticker;
        this.directionOfTransaction = directionOfTransaction;
        this.price = price;
        this.numberOfShares = numberOfShares;
        this.transactionDate = transactionDate;
        this.userId = userId;
        this.note = note;
    }
}


