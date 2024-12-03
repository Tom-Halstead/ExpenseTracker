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

    private UserDTO loggedInUser;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CategoryService categoryService;


    @CommandLine.Option(names = {"-a", "--add"}, description = "Add a new expense")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete an expense")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all expenses")
    private boolean list;

    @CommandLine.Option(names = {"-u", "--update"}, description = "Update an existing expense")
    private boolean update;

    private Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ExpenseCommand() {
    }

    @Override
    public void run() {
        if (add) {
            addExpense();
        } else if (delete) {
            deleteExpense();
        } else if (list) {
            listExpenses();
        } else if (update) {
            updateExpense();
        } else {
            System.out.println("Please specify an option: --add, --delete, --list, or --update");
        }
    }

    private void addExpense() {
        System.out.print("Enter category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter amount: ");
        BigDecimal amount = new BigDecimal(scanner.nextLine());

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter date and time (yyyy-MM-dd HH:mm): ");
        LocalDateTime date = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);

        boolean recurring = getRecurringInput();

        LocalDateTime now = LocalDateTime.now();

        ExpenseDTO expenseDTO = new ExpenseDTO(0, description, amount, date, recurring, categoryId, null, userId, now, now);
        expenseService.addExpense(expenseDTO);
        System.out.println("Expense added: " + expenseDTO);
    }

    private boolean getRecurringInput() {
        System.out.print("Is this a recurring expense? (yes/no): ");
        return "yes".equalsIgnoreCase(scanner.nextLine());
    }

    private void deleteExpense() {
        System.out.print("Enter expense ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        expenseService.deleteExpenseById(id);
        System.out.println("Expense deleted: ID = " + id);
    }

    private void listExpenses() {
        List<ExpenseDTO> expenses = expenseService.getAllExpenses();
        expenses.forEach(expense -> System.out.println(expense));
    }

    private void updateExpense() {
        System.out.print("Enter the ID of the expense to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        ExpenseDTO existingExpense = expenseService.getExpenseById(id);

        System.out.print("Enter new amount (leave blank to keep current): ");
        String amountInput = scanner.nextLine();
        BigDecimal amount = amountInput.isEmpty() ? existingExpense.getAmount() : new BigDecimal(amountInput);

        System.out.print("Enter new description (leave blank to keep current): ");
        String description = scanner.nextLine().isEmpty() ? existingExpense.getDescription() : scanner.nextLine();

        System.out.print("Enter new date and time (yyyy-MM-dd HH:mm, leave blank to keep current): ");
        String dateInput = scanner.nextLine();
        LocalDateTime date = dateInput.isEmpty() ? existingExpense.getDate() : LocalDateTime.parse(dateInput, dateTimeFormatter);

        boolean recurring = getRecurringInput();

        ExpenseDTO updatedExpense = new ExpenseDTO(id, description, amount, date, recurring, existingExpense.getCategoryId(), existingExpense.getCategoryName(), existingExpense.getUserId(), existingExpense.getCreatedAt(), LocalDateTime.now());
        expenseService.updateExpense(id, updatedExpense);
        System.out.println("Expense updated: " + updatedExpense);
    }

    public UserDTO getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
