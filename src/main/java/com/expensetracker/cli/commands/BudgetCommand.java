package com.expensetracker.cli.commands;

import com.expensetracker.dto.BudgetDTO;
import com.expensetracker.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

@Component
@CommandLine.Command(
        name = "budget",
        description = "Manage budgets."
)
public class BudgetCommand implements Runnable {

    @Autowired
    private BudgetService budgetService;

    @CommandLine.Option(names = {"-a", "--add"}, description = "Add a new budget")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete a budget")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all budgets")
    private boolean list;

    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing budget")
    private boolean update;

    private Scanner scanner = new Scanner(System.in);  // Scanner for user input

    @Override
    public void run() {
        if (add) {
            System.out.println("Adding a new budget...");
            addBudget();
        } else if (delete) {
            System.out.println("Deleting a budget...");
            deleteBudget();
        } else if (list) {
            System.out.println("Listing all budgets...");
            listBudgets();
        } else if (update) {
            System.out.println("Updating an existing budget...");
            updateBudget();
        } else {
            System.out.println("Please specify an option: --add, --delete, --list, or --update");
        }
    }

    private void addBudget() {
        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter year (e.g., 2023): ");
        int year = Integer.parseInt(scanner.nextLine());

        // Create a new BudgetDTO
        BudgetDTO budgetDTO = new BudgetDTO(userId, categoryId, amount, month, year);

        // Save the new budget
        budgetService.addBudget(budgetDTO);

        System.out.println("Budget added: " + budgetDTO.toString());
    }

    private void deleteBudget() {
        System.out.print("Delete by ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        budgetService.deleteBudget(id);
        System.out.println("Budget deleted: ID = " + id);
    }

    private void listBudgets() {
        List<BudgetDTO> budgets = budgetService.getAllBudgets();
        if (budgets.isEmpty()) {
            System.out.println("No budgets found.");
        } else {
            budgets.forEach(budget -> System.out.println(budget.toString()));
        }
    }

    private void updateBudget() {
        System.out.print("Enter the ID of the budget to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Fetch the existing budget details
        BudgetDTO existingBudget = budgetService.getBudgetById(id);
        if (existingBudget == null) {
            System.out.println("Budget not found.");
            return;
        }

        System.out.println("Current budget details: " + existingBudget.toString());

        // Prompt for new values and update only if provided
        System.out.print("Enter new amount (leave blank to keep current: " + existingBudget.getAmount() + "): ");
        String amountInput = scanner.nextLine();
        BigDecimal amount = amountInput.isEmpty() ? existingBudget.getAmount() : new BigDecimal(amountInput);

        System.out.print("Enter new category ID (leave blank to keep current: " + existingBudget.getCategoryId() + "): ");
        String categoryInput = scanner.nextLine();
        int categoryId = categoryInput.isEmpty() ? existingBudget.getCategoryId() : Integer.parseInt(categoryInput);

        System.out.print("Enter new user ID (leave blank to keep current: " + existingBudget.getUserId() + "): ");
        String userInput = scanner.nextLine();
        int userId = userInput.isEmpty() ? existingBudget.getUserId() : Integer.parseInt(userInput);

        System.out.print("Enter new month (leave blank to keep current: " + existingBudget.getMonth() + "): ");
        String monthInput = scanner.nextLine();
        int month = monthInput.isEmpty() ? existingBudget.getMonth() : Integer.parseInt(monthInput);

        System.out.print("Enter new year (leave blank to keep current: " + existingBudget.getYear() + "): ");
        String yearInput = scanner.nextLine();
        int year = yearInput.isEmpty() ? existingBudget.getYear() : Integer.parseInt(yearInput);

        // Create an updated BudgetDTO with the modified values
        BudgetDTO updatedBudget = new BudgetDTO(userId, categoryId, amount, month, year);

        // Call the service to update the budget
        budgetService.updateBudget(id, updatedBudget);
        System.out.println("Budget updated: " + updatedBudget.toString());
    }
}