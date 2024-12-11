package com.expensetracker.cli.commands;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
@CommandLine.Command(
        name = "expense",
        description = "Commands related to managing expenses."
)
public class ExpenseCommand implements Runnable {

    @Autowired
    private Scanner scanner;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    MainCommand mainCommand;

    private UserDTO loggedInUser;

    @CommandLine.Option(names = {"-a", "--add"}, description = "Add a new expense")
    private boolean add;
    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete an expense")
    private boolean delete;
    @CommandLine.Option(names = {"-l", "--list"}, description = "List all expenses")
    private boolean list;
    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing expense")
    private boolean update;
    @CommandLine.Option(names = {"-e", "--exit"}, description = "Exit the expense command")
    private boolean exit;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void run() {
        while (true) {
            System.out.println("Enter command (add, delete, list, update, exit):");
            String action = scanner.nextLine().trim().toLowerCase();

            switch (action) {
                case "add":
                    System.out.println("Add Expense Menu:");
                    addExpense();
                    break;
                case "delete":
                    System.out.println("Delete Expense Menu:");
                    deleteExpense();
                    break;
                case "list":
                    System.out.println("Listing Expenses:");
                    listExpenses();
                    break;
                case "update":
                    System.out.println("Update Expense Menu:");
                    updateExpense();
                    break;
                case "exit":
                    System.out.println();
                    System.out.println("Exiting Expense Management...");
                    System.out.println();
                    mainCommand.run();
                    return;
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }

    private void addExpense() {
        System.out.print("Enter category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        LocalDateTime date = LocalDateTime.now();

        boolean recurring = getRecurringInput();

        LocalDateTime now = LocalDateTime.now();

        String source = "";

        ExpenseDTO expenseDTO = new ExpenseDTO(getLoggedInUser().getId(), categoryId, amount, date, description, source, now, now);
        expenseService.addExpense(expenseDTO);
        System.out.println("Expense added: " + expenseDTO);
    }


    private void deleteExpense() {
        System.out.print("Enter expense ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        expenseService.deleteExpenseById(id);
        System.out.println("Expense deleted: ID = " + id);
    }

    private void listExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses found.");
        } else {
            expenses.forEach(System.out::println);
        }
    }

    private void updateExpense() {
        System.out.print("Enter the ID of the expense to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        ExpenseDTO existingExpense = expenseService.getExpenseById(id);

        System.out.println("Current expense details: " + existingExpense);

        System.out.print("Enter new amount (leave blank to keep current): ");
        String amountInput = scanner.nextLine();
        BigDecimal amount = amountInput.isEmpty() ? existingExpense.getAmount() : new BigDecimal(amountInput);

        System.out.print("Enter new description (leave blank to keep current): ");
        String descriptionInput = scanner.nextLine();
        String description = descriptionInput.isEmpty() ? existingExpense.getDescription() : descriptionInput;

        System.out.print("Enter new date and time (yyyy-MM-dd HH:mm, leave blank to keep current): ");
        String dateInput = scanner.nextLine();
        LocalDateTime date = dateInput.isEmpty() ? existingExpense.getDate() : LocalDateTime.parse(dateInput, dateTimeFormatter);

        boolean recurring = getRecurringInput();

        ExpenseDTO updatedExpense = new ExpenseDTO(
                id,
                description,
                amount,
                date,
                recurring,
                existingExpense.getCategoryId(),
                existingExpense.getCategoryName(),
                existingExpense.getUserId(),
                existingExpense.getCreatedAt(),
                LocalDateTime.now()
        );

        ExpenseDTO result = expenseService.updateExpense(id, updatedExpense);
        System.out.println();
        System.out.println("Expense updated with an ID of: " + result.getId());
        System.out.println();
    }



    private boolean getRecurringInput() {
        System.out.print("Is this a recurring expense? (yes/no): ");
        return "yes".equalsIgnoreCase(scanner.nextLine());
    }

    public UserDTO getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
