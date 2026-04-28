package com.pluralsight;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static String filePath = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        showHomeScreen();
    }

    // Main menu
    public static void showHomeScreen() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Accounting Ledger App ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    addPayment();
                    break;
                case "L":
                    showLedgerMenu();
                    break;
                case "X":
                    System.out.println("Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Add money coming into the account
    public static void addDeposit() {
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            String date = java.time.LocalDate.now().toString();
            String time = java.time.LocalTime.now().toString();

            String line = date + "|" + time + "|" + description + "|" + vendor + "|" + amount;

            java.io.FileWriter writer = new java.io.FileWriter(filePath, true);
            writer.write(line + "\n");
            writer.close();

            System.out.println("Deposit added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding deposit.");
        }
    }

    // Add money going out of the account
    public static void addPayment() {
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            amount = -Math.abs(amount);

            String date = java.time.LocalDate.now().toString();
            String time = java.time.LocalTime.now().toString();

            String line = date + "|" + time + "|" + description + "|" + vendor + "|" + amount;

            java.io.FileWriter writer = new java.io.FileWriter(filePath, true);
            writer.write(line + "\n");
            writer.close();

            System.out.println("Payment added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding payment.");
        }
    }

    // Ledger menu
    public static void showLedgerMenu() {
        boolean inLedger = true;

        while (inLedger) {
            System.out.println("\n=== Ledger Menu ===");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    showLedger();
                    break;
                case "D":
                    showDeposits();
                    break;
                case "P":
                    showPayments();
                    break;
                case "R":
                    showReportsMenu();
                    break;
                case "H":
                    inLedger = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Show all transactions
    public static void showLedger() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;

            System.out.println("\n=== Ledger ===");

            while ((line = reader.readLine()) != null) {
                printTransaction(line);
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading ledger.");
        }
    }

    // Show only deposits
    public static void showDeposits() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;
            boolean found = false;

            System.out.println("\n=== Deposits ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                double amount = Double.parseDouble(parts[4]);

                if (amount > 0) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No deposits found.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading deposits.");
        }
    }

    // Show only payments
    public static void showPayments() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;
            boolean found = false;

            System.out.println("\n=== Payments ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                double amount = Double.parseDouble(parts[4]);

                if (amount < 0) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No payments found.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading payments.");
        }
    }

    // Reports menu
    public static void showReportsMenu() {
        boolean inReports = true;

        while (inReports) {
            System.out.println("\n=== Reports Menu ===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showMonthToDate();
                    break;
                case "2":
                    showPreviousMonth();
                    break;
                case "3":
                    showYearToDate();
                    break;
                case "4":
                    showPreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    customSearch();
                    break;
                case "0":
                    inReports = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Month to date report
    public static void showMonthToDate() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;
            boolean found = false;
            java.time.LocalDate now = java.time.LocalDate.now();

            System.out.println("\n=== Month To Date ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                java.time.LocalDate transactionDate = java.time.LocalDate.parse(parts[0]);

                if (transactionDate.getMonth() == now.getMonth()
                        && transactionDate.getYear() == now.getYear()) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found for this month.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading Month To Date.");
        }
    }

    // Previous month report
    public static void showPreviousMonth() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;
            boolean found = false;
            java.time.LocalDate previousMonth = java.time.LocalDate.now().minusMonths(1);

            System.out.println("\n=== Previous Month ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                java.time.LocalDate transactionDate = java.time.LocalDate.parse(parts[0]);

                if (transactionDate.getMonth() == previousMonth.getMonth()
                        && transactionDate.getYear() == previousMonth.getYear()) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found for previous month.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading Previous Month.");
        }
    }

    // Year to date report
    public static void showYearToDate() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;
            boolean found = false;
            int currentYear = java.time.LocalDate.now().getYear();

            System.out.println("\n=== Year To Date ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                java.time.LocalDate transactionDate = java.time.LocalDate.parse(parts[0]);

                if (transactionDate.getYear() == currentYear) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found for this year.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading Year To Date.");
        }
    }

    // Previous year report
    public static void showPreviousYear() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;
            boolean found = false;
            int previousYear = java.time.LocalDate.now().getYear() - 1;

            System.out.println("\n=== Previous Year ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                java.time.LocalDate transactionDate = java.time.LocalDate.parse(parts[0]);

                if (transactionDate.getYear() == previousYear) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found for previous year.");
            }

            reader.close();

        } catch (Exception e) {System.out.println("Error reading Previous Year.");
        }
    }

    // Search transactions by vendor name
    public static void searchByVendor() {
        System.out.print("Enter vendor name: ");
        String input = scanner.nextLine().trim().toLowerCase();

        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;
            boolean found = false;

            System.out.println("\n=== Search by Vendor ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String vendor = parts[3].toLowerCase();

                if (vendor.contains(input)) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found for that vendor.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error searching by vendor.");
        }
    }

    // Print one transaction in a clean readable format
    public static void printTransaction(String line) {
        String[] parts = line.split("\\|");

        System.out.println(
                "Date: " + parts[0] +
                        " | Time: " + parts[1] +
                        " | Description: " + parts[2] +
                        " | Vendor: " + parts[3] +
                        " | Amount: " + parts[4]
        );
    }
    // Bonus: search by optional date, description, vendor, and amount
    public static void customSearch() {
        System.out.print("Start date (YYYY-MM-DD or blank): ");
        String startInput = scanner.nextLine().trim();

        System.out.print("End date (YYYY-MM-DD or blank): ");
        String endInput = scanner.nextLine().trim();

        System.out.print("Description (or blank): ");
        String descriptionInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Vendor (or blank): ");
        String vendorInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Amount (or blank): ");
        String amountInput = scanner.nextLine().trim();

        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(new java.io.FileReader(filePath));

            String line;
            boolean found = false;

            System.out.println("\n=== Custom Search Results ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                java.time.LocalDate transactionDate = java.time.LocalDate.parse(parts[0]);
                String description = parts[2].toLowerCase();
                String vendor = parts[3].toLowerCase();
                double amount = Double.parseDouble(parts[4]);

                boolean matches = true;

                if (!startInput.isEmpty()) {
                    java.time.LocalDate startDate = java.time.LocalDate.parse(startInput);
                    if (transactionDate.isBefore(startDate)) {
                        matches = false;
                    }
                }

                if (!endInput.isEmpty()) {
                    java.time.LocalDate endDate = java.time.LocalDate.parse(endInput);
                    if (transactionDate.isAfter(endDate)) {
                        matches = false;
                    }
                }

                if (!descriptionInput.isEmpty() && !description.contains(descriptionInput)) {
                    matches = false;
                }

                if (!vendorInput.isEmpty() && !vendor.contains(vendorInput)) {
                    matches = false;
                }

                if (!amountInput.isEmpty()) {
                    double searchAmount = Double.parseDouble(amountInput);
                    if (amount != searchAmount) {
                        matches = false;
                    }
                }

                if (matches) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions matched your search.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error running custom search.");
        }
    }
}