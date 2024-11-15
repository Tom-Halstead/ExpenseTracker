package com.expensetracker.cli;

import com.expensetracker.cli.commands.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.Scanner;

@Component
public class AppCommandLineRunner implements CommandLineRunner {

    @Autowired
    private CommandLine.IFactory factory;

    @Override
    public void run(String... args) throws Exception {

        // Main command that registers all subcommands
        CommandLine commandLine = new CommandLine(new MainCommand(), factory);

        // Register subcommands
        commandLine.addSubcommand("user", new UserCommand());
        commandLine.addSubcommand("expense", new ExpenseCommand());
        commandLine.addSubcommand("budget", new BudgetCommand());
        commandLine.addSubcommand("income", new IncomeCommand());
        // add other subcommands

        // Handle default help command
        if (args.length == 0) {
            commandLine.usage(System.out);
        } else {
            commandLine.execute(args);
        }
    }
}