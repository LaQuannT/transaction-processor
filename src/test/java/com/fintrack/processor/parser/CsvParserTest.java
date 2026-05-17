package com.fintrack.processor.parser;

import com.fintrack.processor.exception.FileParseException;
import com.fintrack.processor.model.Transaction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {

    final private String dataFolder = "src/test/resources/data/";

    @Test
    void parserThrowsFileParseExceptionWithInvalidFilePath() {
        CsvParser csvParser = new CsvParser();
        try {
            csvParser.parse("unknown_file_path");
        } catch (FileParseException e) {
            assertEquals("Invalid file path: unknown_file_path", e.getMessage());
        }
    }

    @Test
    void ParserReturnsValidTransactions() {
        // should return 3 valid transactions
        CsvParser csvParser = new CsvParser();
        try {
            csvParser.parse(dataFolder + "vaild_transactions.csv");
            List<Transaction> transactions = csvParser.getTransactions();
            assertNotNull(transactions);
            assertEquals(3, transactions.size());
        } catch (FileParseException e) {
        }
    }

    @Test
    void ParserReturnsMalformedTransactionsOnly() {
        // should return 3 malformed transactions and 0 transactions
        CsvParser csvParser = new CsvParser();
        try {
            csvParser.parse(dataFolder + "missing_fields.csv");
            List<String[]> malformedTransactions = csvParser.getMalformedTransaction();
            List<Transaction> transactions = csvParser.getTransactions();
            assertEquals(0, transactions.size());
            assertEquals(3, malformedTransactions.size());
        } catch (FileParseException e) {}
    }
}