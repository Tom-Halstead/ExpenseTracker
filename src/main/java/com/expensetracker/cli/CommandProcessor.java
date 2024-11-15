package com.expensetracker.cli;

import com.expensetracker.dto.ExpenseDTO;
import com.expensetracker.dto.IncomeDTO;
import com.expensetracker.entity.Budget;
import com.expensetracker.service.BudgetService;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.IncomeService;
import com.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CommandProcessor {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private UserService userService;

    @Autowired
    private BudgetService budgetService;

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void processCommand(String command) {
        String[] args = command.split("\\s+");
        if (args.length < 2) {
            System.out.println("Invalid command. Try again.");
            return;
        }

        String action = args[0];
        String type = args[1];

        try {
            switch (action) {
                case "add":
                    processAddCommand(type, args);
                    break;
                case "delete":
                    processDeleteCommand(type, args);
                    break;
                case "list":
                    processListCommand(type);
                    break;
                default:
                    System.out.println("Unknown command action.");
            }
        } catch (Exception e) {
            System.out.println("Error processing command: " + e.getMessage());
        }
    }

    private void processAddCommand(String type, String[] args) {
        if (type.equals("expense")) {
            if (args.length < 5) {
                System.out.println("Not enough arguments for adding expense.");
                return;
            }
            BigDecimal amount = new BigDecimal(args[2]);
            String description = args[3].replace("'", "");
            LocalDate date = LocalDate.parse(args[4], DATE_FORMAT);
            ExpenseDTO expenseDTO = new ExpenseDTO( amount, description, date);
            expenseService.addExpense(expenseDTO);
            System.out.println("Expense added successfully.");
        } else if (type.equals("income")) {
            if (args.length < 5) {
                System.out.println("Not enough arguments for adding income.");
                return;
            }
            BigDecimal amount = new BigDecimal(args[2]);
            String description = args[3].replace("'", "");
            LocalDate date = LocalDate.parse(args[4], DATE_FORMAT);
            IncomeDTO incomeDTO = new IncomeDTO(amount, description, date);
            incomeService.addIncome(incomeDTO);
            System.out.println("Income added successfully.");
        } else {
            System.out.println("Unknown entity type for add command.");
        }
    }

    private void processDeleteCommand(String type, String[] args) {
        if (type.equals("expense")) {
            if (args.length < 3) {
                System.out.println("Not enough arguments for deleting expense.");
                return;
            }
            int id = Integer.parseInt(args[2]);
            expenseService.deleteExpense(id);
            System.out.println("Expense deleted successfully.");
        } else {
            System.out.println("Delete operation not supported for type: " + type);
        }
    }

    private void processListCommand(String type) {
        if (type.equals("expenses")) {
            List<ExpenseDTO> expenses = expenseService.getAllExpenses();
            if (expenses.isEmpty()) {
                System.out.println("No expenses found.");
            } else {
                expenses.forEach(e -> System.out.println(e));
            }
        } else {
            System.out.println("List operation not supported for type: " + type);
        }
    }
}
