package ru.zinin.myshares.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionDto {

    private Long id;
    private String ticker;
    private boolean directionOfTransaction;
    private double price;
    private int numberOfShares;
    private long transactionDate;
    private String cusip;
    private String description;
    private double lastPrice;
    private String note;

}
