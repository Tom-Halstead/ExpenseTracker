package com.expensetracker.cli.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "income", description = "Manage incomes.")
public class IncomeCommand implements Runnable {

    @CommandLine.Option(names = {"-a", "--add"}, description = "Add new income")
    private boolean add;

    @CommandLine.Parameters(paramLabel = "AMOUNT", description = "Income amount")
    private double amount;

    @Override
    public void run() {
        if (add) {
            System.out.println("Adding new income: $" + amount);
            // Here, you would call your service layer to add the income
        }
    }
}