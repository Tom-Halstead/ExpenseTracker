package com.expensetracker.cli.commands;

import com.expensetracker.dto.BudgetDTO;
import com.expensetracker.dto.UserDTO;
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
public class BudgetCommand implements Command {

    @Autowired
    private BudgetService budgetService;
    private UserDTO loggedInUser;
    private Scanner scanner = new Scanner(System.in);

    @CommandLine.Option(names = {"-a", "--add"}, description = "Add a new budget")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete a budget")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all budgets")
    private boolean list;

    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing budget")
    private boolean update;

    public BudgetCommand() {
        // Default constructor for Spring to manage
    }

    @Override
    public void execute() {
        if (add) {
            addBudget();
        } else if (delete) {
            deleteBudget();
        } else if (list) {
            listBudgets();
        } else if (update) {
            updateBudget();
        } else {
            System.out.println("Please specify an option: --add, --delete, --list, or --update");
        }
    }

    private void addBudget() {
        System.out.println("Adding a new budget...");
        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());
        System.out.print("Enter category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter year (e.g., 2023): ");
        int year = Integer.parseInt(scanner.nextLine());

        BudgetDTO budgetDTO = new BudgetDTO(loggedInUser.getId(), categoryId, amount, month, year);
        budgetService.addBudget(budgetDTO);
        System.out.println("Budget added: " + budgetDTO.toString());
    }

    private void deleteBudget() {
        System.out.print("Enter budget ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        budgetService.deleteBudget(id);
        System.out.println("Budget deleted: ID = " + id);
    }

    private void listBudgets() {
        List<BudgetDTO> budgets = budgetService.getAllBudgetsForUser(loggedInUser.getId());
        if (budgets.isEmpty()) {
            System.out.println("No budgets found.");
        } else {
            budgets.forEach(budget -> System.out.println(budget.toString()));
        }
    }

    private void updateBudget() {
        System.out.print("Enter the ID of the budget to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        BudgetDTO existingBudget = budgetService.getBudgetById(id);
        if (existingBudget == null) {
            System.out.println("Budget not found.");
            return;
        }

        System.out.println("Current budget details: " + existingBudget.toString());
        System.out.print("Enter new amount (leave blank to keep current: " + existingBudget.getAmount() + "): ");
        String amountInput = scanner.nextLine();
        BigDecimal amount = amountInput.isEmpty() ? existingBudget.getAmount() : new BigDecimal(amountInput);

        BudgetDTO updatedBudget = new BudgetDTO(loggedInUser.getId(), existingBudget.getCategoryId(), amount, existingBudget.getMonth(), existingBudget.getYear());
        budgetService.updateBudget(id, updatedBudget);
        System.out.println("Budget updated: " + updatedBudget.toString());
    }

    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public UserDTO getLoggedInUser() {
        return loggedInUser;
    }
}
