package com.expensetracker.cli.commands;

import com.expensetracker.cli.commands.interfaces.UserAwareCommand;
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


    @CommandLine.Option(names = {"-a", "--add"}, description = "Add new income")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete an income")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all incomes")
    private boolean list;

    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing income")
    private boolean update;

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
                    System.out.println("Exiting...");
                    return;  // Exit the loop and end the method
                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }





    private void addIncome() {
        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter source: ");
        String source = scanner.nextLine();

        System.out.print("Enter category ID: ");
        categoryService.getAllCategories();
        int categoryId = Integer.parseInt(scanner.nextLine());

        LocalDateTime now = LocalDateTime.now();

        IncomeDTO incomeDTO = new IncomeDTO(loggedInUser.getId(), categoryId, amount, LocalDateTime.now(), description, source, now, now);
        incomeService.addIncome(incomeDTO);
        System.out.println();
        System.out.println("Income added: " + incomeDTO.toString());
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
        System.out.print("Enter the ID of the income to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        IncomeDTO existingIncome = incomeService.getIncomeById(id);

        System.out.println("Current income details: " + existingIncome.toString());

        System.out.print("Enter new amount (leave blank to keep current): ");
        String amountInput = scanner.nextLine();
        BigDecimal amount = amountInput.isEmpty() ? existingIncome.getAmount() : new BigDecimal(amountInput);

        System.out.print("Enter new description (leave blank to keep current): ");
        String userInput = scanner.nextLine();
        String description = userInput.isEmpty() ? existingIncome.getDescription() : userInput;

        System.out.print("Enter new source (leave blank to keep current): ");
        userInput = scanner.nextLine();
        String source = userInput.isEmpty() ? existingIncome.getSource() : userInput;

        LocalDateTime date = existingIncome.getDate();

        LocalDateTime now = LocalDateTime.now();
        IncomeDTO updatedIncome = new IncomeDTO(id, existingIncome.getUserId(), existingIncome.getCategoryId(), amount, date, description, source, existingIncome.getCreatedAt(), now);
        incomeService.updateIncome(id, updatedIncome);
        System.out.println("Income updated: " + updatedIncome.toString());
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
