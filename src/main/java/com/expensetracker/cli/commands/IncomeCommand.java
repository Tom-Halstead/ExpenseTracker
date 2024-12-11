package com.expensetracker.cli.commands;

import com.expensetracker.cli.commands.interfaces.UserAwareCommand;
import com.expensetracker.dto.CategoryDTO;
import com.expensetracker.dto.IncomeDTO;
import com.expensetracker.dto.UserDTO;
import com.expensetracker.service.CategoryService;
import com.expensetracker.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

@Component
@CommandLine.Command(name = "income", description = "Manage incomes.")
public class IncomeCommand implements UserAwareCommand {

    private UserDTO loggedInUser;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Scanner scanner;

    @Autowired
    private MainCommand mainCommand;


    @CommandLine.Option(names = {"-a", "--add"}, description = "Add new income")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete an income")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all incomes")
    private boolean list;

    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing income")
    private boolean update;
    @CommandLine.Option(names = {"-e", "--exit"}, description = "Exit the expense command")
    private boolean exit;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public IncomeCommand() {
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Enter command (add, delete, list, update, exit):");
            String action = scanner.nextLine().trim().toLowerCase();

            switch (action) {
                case "add":
                    System.out.println("Add Income Menu:");
                    addIncome();
                    break;
                case "delete":
                    System.out.println("Delete Income Menu:");
                    deleteIncome();
                    break;
                case "list":
                    System.out.println("Listing unique incomes:");
                    listIncomes();
                    break;
                case "update":
                    System.out.println("Update Income Menu:");
                    updateIncome();
                    break;
                case "exit":
                    System.out.println();
                    System.out.println("Exiting Income Management...");
                    System.out.println();
                    mainCommand.run();
                    return;
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }





    private void addIncome() {
        Scanner scanner = new Scanner(System.in);

        // Enter amount
        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());  // Consider validating this input

        // Enter description
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        // Enter source
        System.out.print("Enter source: ");
        String source = scanner.nextLine();

        // Display all categories for user reference
        categoryService.getAllCategories();

        // Enter category ID
        System.out.print("Enter category ID (leave blank to create a default category): ");
        String categoryIdInput = scanner.nextLine();
        int categoryId;

        if (categoryIdInput.isBlank()) {
            // Input is blank, create a default category
            CategoryDTO newCategory = categoryService.createOrGetDefaultCategory(getLoggedInUser().getId());
            categoryService.addCategory(newCategory);
            categoryId = newCategory.getCategoryId();
            System.out.println("Default category created with ID: " + categoryId);
        } else {
            try {
                // Parse the entered category ID
                categoryId = Integer.parseInt(categoryIdInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid category ID. Using default category instead.");
                CategoryDTO newCategory = categoryService.createOrGetDefaultCategory(getLoggedInUser().getId());
                categoryService.addCategory(newCategory);
                categoryId = newCategory.getCategoryId();
            }
        }

        // Create IncomeDTO with all the inputs
        LocalDateTime now = LocalDateTime.now();
        IncomeDTO incomeDTO = new IncomeDTO(getLoggedInUser().getId(), categoryId, amount, now, description, source, now, now);

        // Add income
        incomeService.addIncome(incomeDTO);
        System.out.println();
        System.out.println("Income added: " + incomeDTO);
    }


    private void deleteIncome() {
        System.out.print("Enter income ID to delete: ");
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
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the ID of the income to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        IncomeDTO existingIncome = incomeService.getIncomeById(id);

        System.out.println("Current income details: " + existingIncome);

        // Use a single variable to capture user input for different fields
        System.out.print("Enter new amount (leave blank to keep current): ");
        String userInput = scanner.nextLine();
        BigDecimal amount = userInput.isEmpty() ? existingIncome.getAmount() : new BigDecimal(userInput);

        System.out.print("Enter new description (leave blank to keep current): ");
        userInput = scanner.nextLine();
        String description = userInput.isEmpty() ? existingIncome.getDescription() : userInput;

        System.out.print("Enter new source (leave blank to keep current): ");
        userInput = scanner.nextLine();
        String source = userInput.isEmpty() ? existingIncome.getSource() : userInput;

        LocalDateTime now = LocalDateTime.now();
        IncomeDTO updatedIncome = new IncomeDTO(id, existingIncome.getUserId(), existingIncome.getCategoryId(), amount, existingIncome.getDate(), description, source, existingIncome.getCreatedAt(), now);
        incomeService.updateIncome(id, updatedIncome);
        System.out.println("Income updated: " + updatedIncome);
    }


    public UserDTO getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public void enterCommand() {
        System.out.println("Enter command (manage incomes, manage expenses, manage budgets, manage categories, logout): ");
    }
}
