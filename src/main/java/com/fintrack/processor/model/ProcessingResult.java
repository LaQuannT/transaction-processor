package com.fintrack.processor.model;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingResult {

    private List<Transaction> transactions;
    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private BigDecimal netBalance;
    private Map<Category, BigDecimal> byCategory;
    private Map<YearMonth, BigDecimal> byMonth;
    private List<Transaction> topExpenses;
    private List<String> warnings;

    public ProcessingResult(List<Transaction> transactions, BigDecimal totalCredits, BigDecimal totalDebits, BigDecimal netBalance) {
        this.transactions = transactions;
        this.totalCredits = totalCredits;
        this.totalDebits = totalDebits;
        this.netBalance = netBalance;
        this.byCategory = new HashMap<>();
        this.byMonth = new HashMap<>();
        this.topExpenses = new ArrayList<>();
        this.warnings = new ArrayList<>();
    }
}