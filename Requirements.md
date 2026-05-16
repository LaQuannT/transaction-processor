# Transaction Processor — Requirements

A pure Java batch processing application that reads a CSV file of bank transactions, processes them, and outputs a financial summary report.

---

## Input File Format

```csv
date,description,amount,type,category
2024-01-15,Netflix Subscription,-15.99,DEBIT,ENTERTAINMENT
2024-01-16,Salary Deposit,3500.00,CREDIT,INCOME
2024-01-17,Grocery Store,-87.43,DEBIT,FOOD
2024-01-18,Electric Bill,-120.00,DEBIT,UTILITIES
2024-01-19,Freelance Payment,500.00,CREDIT,INCOME
```

**Transaction types:** `CREDIT` or `DEBIT`

**Categories:** `INCOME`, `FOOD`, `UTILITIES`, `ENTERTAINMENT`, `TRANSPORT`, `HEALTHCARE`, `OTHER`

---

## Output

The app takes the CSV file path as a command line argument, processes it, and prints a report to the console and writes it to an output text file.

```
================================================
        TRANSACTION PROCESSING REPORT
================================================
Report Generated  : 2024-01-31
Input File        : transactions.csv
Total Records     : 45
------------------------------------------------

ACCOUNT SUMMARY
------------------------------------------------
Total Credits     :  $4,850.00
Total Debits      : -$2,341.67
Net Balance       :  $2,508.33

SPENDING BY CATEGORY
------------------------------------------------
INCOME            :  $4,850.00
FOOD              :   -$432.11
UTILITIES         :   -$340.00
ENTERTAINMENT     :    -$89.97
TRANSPORT         :   -$210.00
HEALTHCARE        :   -$180.00
OTHER             :    -$89.59

MONTHLY BREAKDOWN
------------------------------------------------
January 2024      :  $1,250.00
February 2024     :    $890.45

TOP 5 LARGEST EXPENSES
------------------------------------------------
1. Rent Payment          -$1,200.00  2024-01-01
2. Car Insurance           -$180.00  2024-01-22
3. Electric Bill           -$120.00  2024-01-18
4. Grocery Store            -$87.43  2024-01-17
5. Phone Bill               -$75.00  2024-01-25

POTENTIAL ISSUES
------------------------------------------------
⚠ 2 duplicate transactions detected
⚠ 3 transactions with missing category
⚠ 1 transaction with invalid amount

================================================
        END OF REPORT
================================================
```

---

## Tech Stack

| Concern | Choice |
|---|---|
| Language | Java 21 |
| Build tool | Maven |
| Testing | JUnit 5 |
| IDE | IntelliJ IDEA |

No external dependencies beyond JUnit 5 for testing. Pure Java only.

---

## Project Structure

```
transaction-processor/
├── src/
│   └── main/
│       └── java/
│           └── com/fintrack/processor/
│               ├── Main.java                     # entry point, wires everything together
│               ├── model/
│               │   ├── Transaction.java          # core domain object
│               │   ├── TransactionType.java      # CREDIT / DEBIT enum
│               │   ├── Category.java             # spending category enum
│               │   └── ProcessingResult.java     # record holding processed data
│               ├── parser/
│               │   ├── CsvParser.java            # reads and parses the CSV
│               │   └── TransactionValidator.java # validates each row
│               ├── processor/
│               │   ├── TransactionProcessor.java # core processing logic
│               │   └── SummaryCalculator.java    # calculates totals and breakdowns
│               ├── report/
│               │   ├── ReportGenerator.java      # builds the report string
│               │   └── FileWriter.java           # writes report to disk
│               └── exception/
│                   ├── InvalidTransactionException.java
│                   └── FileParseException.java
├── src/
│   └── test/
│       └── java/
│           └── com/fintrack/processor/
│               ├── parser/
│               │   └── CsvParserTest.java
│               ├── parser/
│               │   └── TransactionValidatorTest.java
│               ├── processor/
│               │   └── SummaryCalculatorTest.java
│               └── report/
│                   └── ReportGeneratorTest.java
├── data/
│   └── transactions.csv                          # sample input file
├── output/                                       # generated reports land here
└── pom.xml
```

---

## Data Models

### Transaction.java
The core domain object representing a single row from the CSV.

| Field | Type | Description |
|---|---|---|
| `date` | `LocalDate` | Date of the transaction |
| `description` | `String` | Merchant or transaction name |
| `amount` | `BigDecimal` | Transaction amount (negative for debits) |
| `type` | `TransactionType` | CREDIT or DEBIT |
| `category` | `Category` | Spending category |

### TransactionType.java
Enum with two values: `CREDIT`, `DEBIT`

### Category.java
Enum with values: `INCOME`, `FOOD`, `UTILITIES`, `ENTERTAINMENT`, `TRANSPORT`, `HEALTHCARE`, `OTHER`

### ProcessingResult.java
A Java record holding all processed data passed to the report generator.

| Field | Type | Description |
|---|---|---|
| `transactions` | `List<Transaction>` | All valid transactions |
| `totalCredits` | `BigDecimal` | Sum of all credits |
| `totalDebits` | `BigDecimal` | Sum of all debits |
| `netBalance` | `BigDecimal` | Credits minus debits |
| `byCategory` | `Map<Category, BigDecimal>` | Totals grouped by category |
| `byMonth` | `Map<YearMonth, BigDecimal>` | Totals grouped by month |
| `topExpenses` | `List<Transaction>` | Top 5 largest debits |
| `warnings` | `List<String>` | Validation warnings |

---

## Component Responsibilities

### CsvParser.java
- Reads the CSV file line by line using `BufferedReader`
- Skips the header row
- Converts each valid row into a `Transaction` object
- Collects malformed rows as warnings without crashing
- Throws `FileParseException` if the file cannot be read

### TransactionValidator.java
- Detects duplicate transactions (same date, description, and amount)
- Flags transactions with missing or unrecognised categories
- Flags transactions with invalid or null amounts
- Returns valid transactions and a list of warning messages

### TransactionProcessor.java
- Orchestrates the full processing pipeline
- Calls `CsvParser` → `TransactionValidator` → `SummaryCalculator`
- Returns a fully populated `ProcessingResult`

### SummaryCalculator.java
- Calculates total credits, total debits, and net balance
- Groups transactions by category using streams and `Collectors.groupingBy`
- Groups transactions by month using `YearMonth`
- Sorts and returns the top 5 largest expenses

### ReportGenerator.java
- Takes a `ProcessingResult` and formats it into the report structure
- Returns the report as a formatted `String`
- Handles alignment and currency formatting with `String.format`

### FileWriter.java
- Writes the report string to a `.txt` file in the `output/` directory
- Uses `BufferedWriter` and `Files`
- Output filename format: `report_yyyy-MM-dd_HHmmss.txt`

### Main.java
- Entry point — accepts file path as a command line argument
- Validates that an argument was provided and the file exists
- Wires all components together in order
- Prints the report to console and triggers file write

---

## Java Concepts Covered

| Concept | Where you use it |
|---|---|
| Classes and objects | `Transaction`, `CsvParser`, all components |
| Enums with values | `TransactionType`, `Category` |
| Records | `ProcessingResult` |
| Interfaces | Define `Parser` and `Generator` interfaces, implement them |
| Generics | `List<Transaction>`, `Map<Category, BigDecimal>` |
| Optional | Handling missing values in parser |
| Exception handling | `FileParseException`, `InvalidTransactionException`, try-catch-finally |
| Streams | Filtering, mapping, grouping, sorting transactions |
| Collectors | `groupingBy`, `summingDouble`, `toList`, `toUnmodifiableList` |
| Comparator | Sorting top expenses by amount |
| LocalDate / YearMonth | Parsing dates, grouping by month |
| DateTimeFormatter | Custom date format parsing |
| BigDecimal | Precise financial calculations — never use double for money |
| BufferedReader / BufferedWriter | File reading and writing |
| Path / Files | File existence checks, path construction |
| String.format | Aligned report output |

---

## Error Handling Rules

- The app must never crash on a bad row — collect the error, skip the row, continue processing
- If the input file does not exist, print a clear error message and exit with code 1
- If the input file is empty or has no valid transactions after the header, print a clear message and exit
- All warnings are collected and printed in the POTENTIAL ISSUES section of the report
- Use custom exceptions (`FileParseException`, `InvalidTransactionException`) rather than raw `RuntimeException`

---

## Validation Rules

| Rule | Warning message |
|---|---|
| Same date + description + amount appears more than once | `Duplicate transaction detected: {description} on {date}` |
| Category field is empty or not a known enum value | `Unknown category on row {n}: {value} — defaulted to OTHER` |
| Amount field is missing, blank, or not a valid number | `Invalid amount on row {n}: {value} — row skipped` |
| Date field is missing or not in expected format | `Invalid date on row {n}: {value} — row skipped` |

---

## Testing Requirements

Write JUnit 5 tests for each of the following:

### CsvParserTest
- Parses a valid CSV file and returns correct number of transactions
- Skips malformed rows and adds warnings
- Throws `FileParseException` when file does not exist
- Handles empty file gracefully

### TransactionValidatorTest
- Detects duplicate transactions correctly
- Flags missing categories
- Flags invalid amounts
- Returns only valid transactions in output

### SummaryCalculatorTest
- Total credits sum is correct
- Total debits sum is correct
- Net balance is correct
- Category grouping returns correct totals
- Monthly grouping returns correct totals
- Top 5 expenses are sorted correctly and are all debits

### ReportGeneratorTest
- Report contains all expected sections
- Currency values are formatted correctly
- Warning section appears when warnings exist
- Warning section is omitted when there are no warnings

---

## Build Order

Follow this exactly. Do not move to the next step until the current one works.

| Step | Task | Done |
|---|---|---|
| 1 | Define `Transaction`, `TransactionType`, `Category`, `ProcessingResult` | ⬜ |
| 2 | Build `CsvParser` — read file, parse rows, handle errors | ⬜ |
| 3 | Build `TransactionValidator` — duplicates, missing fields, bad amounts | ⬜ |
| 4 | Build `SummaryCalculator` — totals, groupings, top expenses | ⬜ |
| 5 | Build `ReportGenerator` — format output string | ⬜ |
| 6 | Build `FileWriter` — write report to disk | ⬜ |
| 7 | Build `Main.java` — wire everything together, run end to end | ⬜ |
| 8 | Write JUnit 5 tests for all components | ⬜ |

---

## Extensions (After Core is Complete)

These are optional. Only attempt after Step 8 is done and all tests pass.

- **JSON input support** — add a `JsonParser` that accepts a JSON transaction file using Jackson. Introduces dependency management without a framework.
- **Config file** — allow users to define custom categories in a `config.properties` file. Teaches Java properties loading.
- **Multi-file processing** — accept a directory path and process all CSV files inside it.
- **Diff mode** — accept two CSV files and report what transactions are new, removed, or changed between them.

---

## Sample transactions.csv

Copy this into `data/transactions.csv` to use as your test input.

```csv
date,description,amount,type,category
2024-01-01,Rent Payment,-1200.00,DEBIT,UTILITIES
2024-01-02,Salary Deposit,3500.00,CREDIT,INCOME
2024-01-05,Grocery Store,-87.43,DEBIT,FOOD
2024-01-08,Netflix Subscription,-15.99,DEBIT,ENTERTAINMENT
2024-01-10,Electric Bill,-120.00,DEBIT,UTILITIES
2024-01-12,Freelance Payment,500.00,CREDIT,INCOME
2024-01-14,Gas Station,-45.00,DEBIT,TRANSPORT
2024-01-15,Pharmacy,-30.00,DEBIT,HEALTHCARE
2024-01-18,Grocery Store,-92.10,DEBIT,FOOD
2024-01-20,Car Insurance,-180.00,DEBIT,TRANSPORT
2024-01-22,Phone Bill,-75.00,DEBIT,UTILITIES
2024-01-25,Restaurant,-55.00,DEBIT,FOOD
2024-01-28,Spotify,-9.99,DEBIT,ENTERTAINMENT
2024-01-30,ATM Withdrawal,-100.00,DEBIT,OTHER
2024-02-01,Rent Payment,-1200.00,DEBIT,UTILITIES
2024-02-02,Salary Deposit,3500.00,CREDIT,INCOME
2024-02-05,Grocery Store,-110.00,DEBIT,FOOD
2024-02-08,Netflix Subscription,-15.99,DEBIT,ENTERTAINMENT
2024-02-10,Electric Bill,-98.00,DEBIT,UTILITIES
2024-02-14,Gas Station,-50.00,DEBIT,TRANSPORT
2024-02-18,Pharmacy,-45.00,DEBIT,HEALTHCARE
2024-02-20,Restaurant,-70.00,DEBIT,FOOD
2024-02-22,Phone Bill,-75.00,DEBIT,UTILITIES
2024-02-25,Grocery Store,-110.00,DEBIT,FOOD
```