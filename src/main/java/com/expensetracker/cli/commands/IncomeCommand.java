package com.expensetracker.cli.commands;

import com.expensetracker.dto.IncomeDTO;
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

    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

        System.out.print("Enter date and time (yyyy-MM-dd HH:mm): ");
        LocalDateTime date = LocalDateTime.parse(scanner.nextLine(), dateTimeFormatter);

        System.out.print("Enter user ID: ");
        int userId = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter category ID: ");
        int categoryId = Integer.parseInt(scanner.nextLine());

        LocalDateTime now = LocalDateTime.now();

        IncomeDTO incomeDTO = new IncomeDTO(userId, categoryId, amount, date, description, source, now, now);
        incomeService.addIncome(incomeDTO);
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
        String description = scanner.nextLine().isEmpty() ? existingIncome.getDescription() : scanner.nextLine();

        System.out.print("Enter new source (leave blank to keep current): ");
        String source = scanner.nextLine().isEmpty() ? existingIncome.getSource() : scanner.nextLine();

        System.out.print("Enter new date and time (yyyy-MM-dd HH:mm, leave blank to keep current): ");
        String dateInput = scanner.nextLine();
        LocalDateTime date = dateInput.isEmpty() ? existingIncome.getDate() : LocalDateTime.parse(dateInput, dateTimeFormatter);

        LocalDateTime now = LocalDateTime.now();
        IncomeDTO updatedIncome = new IncomeDTO(id, existingIncome.getUserId(), existingIncome.getCategoryId(), amount, date, description, source, existingIncome.getCreatedAt(), now);
        incomeService.updateIncome(id, updatedIncome);
        System.out.println("Income updated: " + updatedIncome.toString());
    }
}
