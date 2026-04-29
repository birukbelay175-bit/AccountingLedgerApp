package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Main {

    // Global tools and constants
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_PATH = "src/main/resources/transactions.csv";
    static final String DELIMITER = "\\|";

    public static void main(String[] args) {
        showHomeScreen();
    }

    // =========================
    // HOME MENU
    // =========================
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

    // =========================
    // ADD TRANSACTION
    // =========================
    public static void addTransaction(boolean isDeposit) {
        try {
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());

            // Make payment negative
            if (!isDeposit) {
                amount = -Math.abs(amount);
            }

            String line = LocalDate.now() + "|" +
                    LocalTime.now().withNano(0) + "|" +
                    description + "|" +
                    vendor + "|" +
                    amount;

            FileWriter writer = new FileWriter(FILE_PATH, true);
            writer.write(line + "\n");
            writer.close();

            System.out.println(isDeposit ? "Deposit added!" : "Payment added!");

        } catch (Exception e) {
            System.out.println("Error adding transaction.");
        }
    }

    // =========================
    // LEDGER MENU
    // =========================
    public static void showLedgerMenu() {
        while (true) {
            System.out.println("\n=== Ledger Menu ===");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().toUpperCase();

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

    // =========================
    // SHOW TRANSACTIONS
    // =========================
    public static void showTransactions(String type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            boolean found = false;

            System.out.println("\n=== " + type + " TRANSACTIONS ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                double amount = Double.parseDouble(parts[4]);

                if (type.equals("ALL") ||
                        (type.equals("DEPOSIT") && amount > 0) ||
                        (type.equals("PAYMENT") && amount < 0)) {

                    printTransaction(parts);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found.");
            }

        } catch (Exception e) {
            System.out.println("Error reading transactions.");
        }
    }

    // =========================
    // REPORTS MENU
    // =========================
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

    // =========================
    // DATE REPORTS
    // =========================
    public static void reportByDate(String type) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            boolean found = false;
            LocalDate today = LocalDate.now();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);
                LocalDate date = LocalDate.parse(parts[0]);

                boolean match = matchesReportType(type, date, today);

                if (match) {
                    printTransaction(parts);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No transactions found.");
            }

        } catch (Exception e) {
            System.out.println("Error reading report.");
        }
    }

    // ========================
    // SEARCH BY VENDOR
    // ========================
    public static void searchByVendor() {
        System.out.print("Enter vendor: ");
        String input = scanner.nextLine().toLowerCase();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(DELIMITER);

                if (parts[3].toLowerCase().contains(input)) {
                    printTransaction(parts);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No matches found.");
            }

        } catch (Exception e) {
            System.out.println("Error searching.");
        }
    }

    // =========================
    // CUSTOM SEARCH (BONUS)
    // =========================
    public static void customSearch() {

        System.out.print("Start date (YYYY-MM-DD or blank): ");
        String start = scanner.nextLine();

        System.out.print("End date (YYYY-MM-DD or blank): ");
        String end = scanner.nextLine();

        System.out.print("Description: ");
        String descInput = scanner.nextLine().toLowerCase();

        System.out.print("Vendor: ");
        String vendorInput = scanner.nextLine().toLowerCase();

        System.out.print("Amount: ");
        String amountInput = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] p = line.split(DELIMITER);

                LocalDate date = LocalDate.parse(p[0]);
                boolean match =
                        (start.isEmpty() || !date.isBefore(LocalDate.parse(start))) &&
                                (end.isEmpty() || !date.isAfter(LocalDate.parse(end))) &&
                                (descInput.isEmpty() || p[2].toLowerCase().contains(descInput)) &&
                                (vendorInput.isEmpty() || p[3].toLowerCase().contains(vendorInput)) &&
                                (amountInput.isEmpty() || p[4].equals(amountInput));

                if (match) {
                    printTransaction(p);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No results.");
            }

        } catch (Exception e) {
            System.out.println("Error in search.");
        }
    }

    // =========================
    // PRINT FORMAT
    // =========================
    public static void printTransaction(String[] p) {
        System.out.println(
                "Date: " + p[0] +
                        " | Time: " + p[1] +
                        " | Description: " + p[2] +
                        " | Vendor: " + p[3] +
                        " | Amount: " + p[4]
        );
    }

    // This method checks which report type we are using
    public static boolean matchesReportType(String type, LocalDate date, LocalDate today) {
        return switch (type) {

            case "MONTH" -> date.getMonth() == today.getMonth()
                    && date.getYear() == today.getYear();

            case "PREVIOUS_MONTH" -> {
                LocalDate lastMonth = today.minusMonths(1);
                yield date.getMonth() == lastMonth.getMonth()
                        && date.getYear() == lastMonth.getYear();
            }

            case "YEAR" -> date.getYear() == today.getYear();

            case "PREVIOUS_YEAR" -> date.getYear() == today.getYear() - 1;

            default -> false;
        };
    }
}