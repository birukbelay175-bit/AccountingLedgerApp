package com.pluralsight;

import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        showHomeScreen();
    }


    public static void showHomeScreen() {
        boolean running = true;

        while (running) {
            System.out.println("\n=== Accounting Ledger App ===");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment");
            System.out.println("1) Show Deposits");
            System.out.println("2) Show Payments");
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
                case "1":
                    showDeposits();
                    break;
                case "2":
                    showPayments();
                    break;

                case "L":
                    showLedgerMenu();
                    break;

                case "X":
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }

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

            java.io.FileWriter writer = new java.io.FileWriter("src/main/resources/transactions.csv", true);
            writer.write(line + "\n");
            writer.close();

            System.out.println("Deposit added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding deposit.");
        }
    }

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

            java.io.FileWriter writer =
                    new java.io.FileWriter("src/main/resources/transactions.csv", true);
            writer.write(line + "\n");
            writer.close();

            System.out.println("Payment added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding payment.");
        }
    }
    public static void showDeposits() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                            new java.io.FileReader("src/main/resources/transactions.csv"));

            String line;

            System.out.println("\n=== Deposits ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                double amount = Double.parseDouble(parts[4]);

                if (amount > 0) {
                    System.out.println(line);
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading deposits.");
        }
    }
    public static void showPayments() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                            new java.io.FileReader("src/main/resources/transactions.csv"));

            String line;

            System.out.println("\n=== Payments ===");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                double amount = Double.parseDouble(parts[4]);

                if (amount < 0) {
                    System.out.println(line);
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading payments.");
        }
    }
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
                    System.out.println("Invalid option");
            }
        }
    }
    public static void showReportsMenu() {
        boolean inReports = true;

        while (inReports) {
            System.out.println("\n=== Reports Menu ===");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Month To Date (coming next)");
                    break;
                case "2":
                    System.out.println("Previous Month (coming next)");
                    break;
                case "3":
                    System.out.println("Year To Date (coming next)");
                    break;
                case "4":
                    System.out.println("Previous Year (coming next)");
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "0":
                    inReports = false;
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
    public static void searchByVendor() {
        System.out.print("Enter vendor name: ");
        String input = scanner.nextLine().toLowerCase();

        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                            new java.io.FileReader("src/main/resources/transactions.csv"));

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                String vendor = parts[3].toLowerCase();

                if (vendor.contains(input)) {
                    System.out.println(line);
                }
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error searching vendor.");
        }
    }

    public static void showLedger() {
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                            new java.io.FileReader("src/main/resources/transactions.csv"));

            String line;

            System.out.println("\n=== Ledger ===");

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();

        } catch (Exception e) {
            System.out.println("Error reading ledger.");
        }
    }
}