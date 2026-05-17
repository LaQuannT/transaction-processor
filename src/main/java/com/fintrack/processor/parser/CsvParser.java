package com.fintrack.processor.parser;

import com.fintrack.processor.exception.FileParseException;
import com.fintrack.processor.model.Category;
import com.fintrack.processor.model.Transaction;
import com.fintrack.processor.model.TransactionType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private List<Transaction> transactions;
    private List<String[]> malformedTransaction;

    public CsvParser(){
        this.transactions = new ArrayList<>();
        this.malformedTransaction = new ArrayList<>();
    }

    public void parse(String filePath) throws FileParseException {
        String delimiter = ",";
        String line;

        if (!filePath.endsWith(".csv"))
                throw new FileParseException("Invalid file path: " + filePath);

        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            while((line = br.readLine()) != null){
                String[] values = line.split(delimiter);

                if (values.length != 5){
                    malformedTransaction.add(values);
                    continue;
                }

                for (String value:values) {
                    if (value.isEmpty()) {
                        malformedTransaction.add(values);
                        break;
                    }
                }
                LocalDate date = LocalDate.parse(values[0]);
                BigDecimal amount = new BigDecimal(values[2]);
                TransactionType type = TransactionType.valueOf(values[3]);
                Category category = Category.valueOf(values[4]);
                Transaction transaction = new Transaction(date, values[1], amount,type, category);
                transactions.add(transaction);
            }

        } catch (IOException e) {
            throw new FileParseException("failed to read" + filePath + ": " + e.getMessage());
        }
    }

    public List<String[]> getMalformedTransaction() {
        return malformedTransaction;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}