# Accounting Ledger App

## Description
Accounting Ledger App is a Java console application that allows users to record deposits and payments, view all transactions, filter transactions, and generate reports. The app saves transaction data in a CSV file using a pipe `|` delimiter.

## Project Structure

```text
AccountingLedgerApp/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── pluralsight/
│       │           ├── Main.java
│       │           └── Transaction.java
│       └── resources/
│           └── transactions.csv
```

## Features

- Add a deposit
- Make a payment
- View all ledger transactions
- View deposits only
- View payments only
- Generate reports:
  - Month to Date
  - Previous Month
  - Year to Date
  - Previous Year
  - Search by Vendor
  - Custom Search
- Save all transactions to `transactions.csv`

## CSV File Format

Transactions are stored in this format:

```text
date|time|description|vendor|amount
```

Example:

```text
2026-04-30|15:45:20|Phone Bill|AT&T|-50.0
2026-04-30|16:00:10|Paycheck|Home Depot|500.0
```

Deposits are saved as positive amounts, and payments are saved as negative amounts.

## How to Run the Program

1. Open the project in IntelliJ IDEA.
2. Make sure the file `transactions.csv` is inside:

```text
src/main/resources/transactions.csv
```

3. Open `Main.java`.
4. Click the green Run button next to the `main` method.
5. Use the console menu to choose an option.

## Home Menu Options

```text
D) Add Deposit
P) Make Payment
L) Ledger
X) Exit
```

## Ledger Menu Options

```text
A) All
D) Deposits
P) Payments
R) Reports
H) Home
```

## Reports Menu Options

```text
1) Month To Date
2) Previous Month
3) Year To Date
4) Previous Year
5) Search by Vendor
6) Custom Search
0) Back
```

## Main Classes

### Main.java
This class contains the main program logic, menus, file writing, file reading, transaction filtering, and reports.

### Transaction.java
This class represents one transaction. It stores the transaction date, time, description, vendor, and amount.

## Technologies Used

- Java
- IntelliJ IDEA
- CSV file storage
- Java `Scanner`
- Java `FileWriter`
- Java `BufferedReader`
- Java `LocalDate` and `LocalTime`

## Notes

- The app uses `src/main/resources/transactions.csv` as the transaction file path.
- The program separates each transaction field using the `|` symbol.
- Payments are automatically converted to negative numbers.
- Deposits stay positive.

## Author

Biruk Tafese
