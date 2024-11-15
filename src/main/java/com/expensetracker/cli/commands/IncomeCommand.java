package com.expensetracker.cli.commands;

import com.expensetracker.dto.IncomeDTO;
import com.expensetracker.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Component
@CommandLine.Command(name = "income", description = "Manage incomes.")
public class IncomeCommand implements Runnable {

    @Autowired
    private IncomeService incomeService;

    @CommandLine.Option(names = {"-a", "--add"}, description = "Add new income")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete an income")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all incomes")
    private boolean list;

    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing income")
    private boolean update;

    private final Scanner scanner = new Scanner(System.in);  // Scanner for user input

    @Override
    public void run() {
        if (add) {
            System.out.println("Adding a new income...");
            addIncome();
        } else if (delete) {
            System.out.println("Deleting an income...");
            deleteIncome();
        } else if (list) {
            System.out.println("Listing all incomes...");
            listIncomes();
        } else if (update) {
            System.out.println("Updating an existing income...");
            updateIncome();
        } else {
            System.out.println("Please specify an option: --add, --delete, --list, or --update");
        }
    }

    private void addIncome() {
        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter source: ");
        String source = scanner.nextLine();

        // Use the current system date for income date
        LocalDate date = LocalDate.now();

        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        // Create an IncomeDTO with the user input
        IncomeDTO incomeDTO = new IncomeDTO(userId, categoryId, amount, date, description, source);

        // Save the new income
        incomeService.addIncome(incomeDTO);

        System.out.println("Income added: " + incomeDTO.toString());
    }

    private void deleteIncome() {
        System.out.print("Delete by ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        incomeService.deleteIncome(id);
        System.out.println("Income deleted: ID = " + id);
    }

    private void listIncomes() {
        List<IncomeDTO> incomes = incomeService.getAllIncomes();
        if (incomes.isEmpty()) {
            System.out.println("No incomes found.");
        } else {
            incomes.forEach(income -> System.out.println(income.toString()));
        }
    }

    private void updateIncome() {
        System.out.print("Enter the ID of the income to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Fetch the existing income details
        IncomeDTO existingIncome = incomeService.getIncomeById(id);
        if (existingIncome == null) {
            System.out.println("Income not found.");
            return;
        }

        System.out.println("Current income details: " + existingIncome.toString());

        // Prompt for new values and update only if provided
        System.out.print("Enter new amount (leave blank to keep current: " + existingIncome.getAmount() + "): ");
        String amountInput = scanner.nextLine();
        BigDecimal amount = amountInput.isEmpty() ? existingIncome.getAmount() : new BigDecimal(amountInput);

        System.out.print("Enter new description (leave blank to keep current: " + existingIncome.getDescription() + "): ");
        String description = scanner.nextLine().isEmpty() ? existingIncome.getDescription() : scanner.nextLine();

        System.out.print("Enter new source (leave blank to keep current: " + existingIncome.getSource() + "): ");
        String source = scanner.nextLine().isEmpty() ? existingIncome.getSource() : scanner.nextLine();

        System.out.print("Enter new date (YYYY-MM-DD, leave blank to keep current: " + existingIncome.getDate() + "): ");
        String dateInput = scanner.nextLine();
        LocalDate date = dateInput.isEmpty() ? existingIncome.getDate() : LocalDate.parse(dateInput);

        // Create an updated IncomeDTO with the modified values
        IncomeDTO updatedIncome = new IncomeDTO(existingIncome.getUserId(), existingIncome.getCategoryId(), amount, date, description, source);

        // Call the service to update the income
        incomeService.updateIncome(id, updatedIncome);
        System.out.println("Income updated: " + updatedIncome.toString());
    }
}