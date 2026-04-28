package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static String filePath = "src/main/resources/transactions.csv";

    public static void main(String[] args) {
        showHomeScreen();
    }

    // Home screen menu
    public static void showHomeScreen() {
        while (true) {
            System.out.println("\n=== Accounting Ledger App ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "D":
                    addTransaction(true);
                    break;
                case "P":
                    addTransaction(false);
                    break;
                case "L":
                    showLedgerMenu();
                    break;
                case "X":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Adds either a deposit or payment
    public static void addTransaction(boolean isDeposit) {
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            // Payments must be negative
            if (!isDeposit) {
                amount = -Math.abs(amount);
            }

            String line = LocalDate.now() + "|" +
                    LocalTime.now().withNano(0) + "|" +
                    description + "|" +
                    vendor + "|" +
                    amount;

            FileWriter writer = new FileWriter(filePath, true);
            writer.write(line + "\n");
            writer.close();

            System.out.println(isDeposit ? "Deposit added successfully!" : "Payment added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding transaction.");
        }
    }

    // Ledger menu
    public static void showLedgerMenu() {
        while (true) {
            System.out.println("\n=== Ledger Menu ===");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    showTransactions("ALL");
                    break;
                case "D":
                    showTransactions("DEPOSIT");
                    break;
                case "P":
                    showTransactions("PAYMENT");
                    break;
                case "R":
                    showReportsMenu();
                    break;
                case "H":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Shows all, deposits, or payments
    public static void showTransactions(String type) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean found = false;

            System.out.println("\n=== " + type + " TRANSACTIONS ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                double amount = Double.parseDouble(parts[4]);

                if (type.equals("ALL")
                        || (type.equals("DEPOSIT") && amount > 0)
                        || (type.equals("PAYMENT") && amount < 0)) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading transactions.");
        }
    }

    // Reports menu
    public static void showReportsMenu() {
        while (true) {
            System.out.println("\n=== Reports Menu ===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    reportByDate("MONTH");
                    break;
                case "2":
                    reportByDate("PREVIOUS_MONTH");
                    break;
                case "3":
                    reportByDate("YEAR");
                    break;
                case "4":
                    reportByDate("PREVIOUS_YEAR");
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "6":
                    customSearch();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Handles all date reports
    public static void reportByDate(String type) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean found = false;
            LocalDate today = LocalDate.now();

            System.out.println("\n=== Report ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                LocalDate date = LocalDate.parse(parts[0]);

                boolean match =
                        (type.equals("MONTH") && date.getMonth() == today.getMonth() && date.getYear() == today.getYear())
                                || (type.equals("PREVIOUS_MONTH") && date.getMonth() == today.minusMonths(1).getMonth() && date.getYear() == today.minusMonths(1).getYear())
                                || (type.equals("YEAR") && date.getYear() == today.getYear())
                                || (type.equals("PREVIOUS_YEAR") && date.getYear() == today.getYear() - 1);

                if (match) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading report.");
        }
    }

    // Search by vendor name
    public static void searchByVendor() {
        System.out.print("Enter vendor name: ");
        String input = scanner.nextLine().trim().toLowerCase();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean found = false;

            System.out.println("\n=== Search by Vendor ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts[3].toLowerCase().contains(input)) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error searching vendor.");
        }
    }

    // Bonus custom search
    public static void customSearch() {
        System.out.print("Start date (YYYY-MM-DD or blank): ");
        String startInput = scanner.nextLine().trim();

        System.out.print("End date (YYYY-MM-DD or blank): ");
        String endInput = scanner.nextLine().trim();

        System.out.print("Description or blank: ");
        String descInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Vendor or blank: ");
        String vendorInput = scanner.nextLine().trim().toLowerCase();

        System.out.print("Amount or blank: ");
        String amountInput = scanner.nextLine().trim();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean found = false;

            System.out.println("\n=== Custom Search ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                LocalDate date = LocalDate.parse(parts[0]);
                String desc = parts[2].toLowerCase();
                String vendor = parts[3].toLowerCase();
                String amount = parts[4];

                boolean match =
                        (startInput.isEmpty() || !date.isBefore(LocalDate.parse(startInput)))
                                && (endInput.isEmpty() || !date.isAfter(LocalDate.parse(endInput)))
                                && (descInput.isEmpty() || desc.contains(descInput))
                                && (vendorInput.isEmpty() || vendor.contains(vendorInput))
                                && (amountInput.isEmpty() || amount.equals(amountInput));
                if (match) {
                    printTransaction(line);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No matching transactions found.");
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error running custom search.");
        }
    }

    // Clean output format
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
}