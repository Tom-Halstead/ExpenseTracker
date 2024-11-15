package com.expensetracker.cli.commands;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

@Component
@CommandLine.Command(
        name = "expense",
        description = "Commands related to managing expenses."
)
public class ExpenseCommand implements Runnable {

    @Autowired
    private ExpenseService expenseService;

    @CommandLine.Option(names = {"-a", "--add"}, description = "Add a new expense")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete an expense")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all expenses")
    private boolean list;

    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing expense")
    private boolean update;

    private Scanner scanner = new Scanner(System.in);  // Scanner for user input

    @Override
    public void run() {
        if (add) {
            System.out.println("Adding a new expense...");
            addExpense();
        } else if (delete) {
            System.out.println("Deleting an expense...");
            deleteExpense();
        } else if (list) {
            System.out.println("Listing all expenses...");
            listExpenses();
        } else if (update) {
            System.out.println("Updating an existing expense...");
            updateExpense();
        } else {
            System.out.println("Please specify an option: --add, --delete, --list, or --update");
        }
    }

    private void addExpense() {
        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine()); // Using BigDecimal

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        // Use the current system date
        LocalDate date = LocalDate.now();

        // Create an ExpenseDTO using BigDecimal for the amount and the current date
        ExpenseDTO expenseDTO = new ExpenseDTO(amount, description, date);

        // Save the new expense
        expenseService.addExpense(expenseDTO);

        System.out.println("Expense added with current date: " + expenseDTO.toString());
    }

    private void deleteExpense() {
        System.out.print("Delete by ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        expenseService.deleteExpenseById(id);
        System.out.println("Expense deleted: ID = " + id);
    }

    private void listExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
        } else {
            expenses.forEach(expense -> System.out.println(expense.toString()));
        }
    }

    private void updateExpense() {
        System.out.print("Enter the ID of the expense to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Fetch the existing expense details
        ExpenseDTO existingExpense = expenseService.getExpenseById(id);
        if (existingExpense == null) {
            System.out.println("Expense not found.");
            return;
        }

        System.out.println("Current expense details: " + existingExpense.toString());

        // Prompt for new values and update only if provided
        System.out.print("Enter new amount (leave blank to keep current: " + existingExpense.getAmount() + "): ");
        String amountInput = scanner.nextLine();
        BigDecimal amount = amountInput.isEmpty() ? existingExpense.getAmount() : new BigDecimal(amountInput);

        System.out.print("Enter new description (leave blank to keep current: " + existingExpense.getDescription() + "): ");
        String description = scanner.nextLine().isEmpty() ? existingExpense.getDescription() : scanner.nextLine();

        System.out.print("Enter new date (YYYY-MM-DD, leave blank to keep current: " + existingExpense.getDate() + "): ");
        String dateInput = scanner.nextLine();
        LocalDate date = dateInput.isEmpty() ? existingExpense.getDate() : LocalDate.parse(dateInput);

        // Create an updated ExpenseDTO with the modified values
        ExpenseDTO updatedExpense = new ExpenseDTO(amount, description, date);

        // Call the service to update the expense
        expenseService.updateExpense(id, updatedExpense);
        System.out.println("Expense updated: " + updatedExpense.toString());
    }
}