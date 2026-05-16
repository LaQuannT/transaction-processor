package com.fintrack.processor.model;


import java.math.BigDecimal;
import java.time.LocalDate;

public class Transaction {

    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private Category category;

    public Transaction(LocalDate date, String description, BigDecimal amount, TransactionType type, Category category) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.category = category;
    }
}