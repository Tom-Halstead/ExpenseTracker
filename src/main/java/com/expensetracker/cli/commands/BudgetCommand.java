package com.expensetracker.cli.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "budget", description = "Manage budgets.")
public class BudgetCommand implements Runnable {

    @CommandLine.Option(names = {"-s", "--set"}, description = "Set a new budget")
    private boolean set;

    @CommandLine.Parameters(paramLabel = "AMOUNT", description = "Budget amount")
    private double amount;

    @Override
    public void run() {
        if (set) {
            System.out.println("Setting a new budget: $" + amount);
            // Here, you would call your service layer to set the budget
        }
    }
}