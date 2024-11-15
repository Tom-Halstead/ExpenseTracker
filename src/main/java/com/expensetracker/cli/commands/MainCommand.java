package com.expensetracker.cli.commands;

import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(
        name = "app",
        description = "Main entry point for the application.",
        mixinStandardHelpOptions = true,
        subcommands = {
                UserCommand.class,
                ExpenseCommand.class,
                IncomeCommand.class,
                BudgetCommand.class,
                CommandLine.HelpCommand.class
        }
)
public class MainCommand implements Runnable {
    @Override
    public void run() {
        // This code runs if no subcommands are invoked.
        System.out.println("Welcome to the Expense Tracker CLI/API");
    }

}