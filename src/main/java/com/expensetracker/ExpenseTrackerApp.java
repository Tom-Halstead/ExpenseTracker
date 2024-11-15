package com.expensetracker;

import com.expensetracker.cli.commands.*;
import com.expensetracker.config.SpringPicocliFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.client.RestTemplate;
import picocli.CommandLine;

import java.util.Scanner;

@SpringBootApplication
public class ExpenseTrackerApp {

        @Autowired
        private ApplicationContext applicationContext;  // Injected ApplicationContext

        public static void main(String[] args) {
                SpringApplication.run(ExpenseTrackerApp.class, args);
        }

        @Bean
        public CommandLine.IFactory picocliFactory() {
                return new SpringPicocliFactory(applicationContext);
        }

        @EventListener(ApplicationReadyEvent.class)
        public void runCLI() {
                // Retrieve the main command bean
                MainCommand mainCommand = applicationContext.getBean(MainCommand.class);

                // Create a CommandLine instance with the factory and main command
                CommandLine commandLine = new CommandLine(mainCommand, picocliFactory());

                // Add CategoryCommand bean to CommandLine instance
                commandLine.addSubcommand("category", applicationContext.getBean(CategoryCommand.class));

                // Start the CLI in a separate thread to keep the server responsive
                new Thread(() -> {
                        try (Scanner scanner = new Scanner(System.in)) {
                                // Display a welcome message and help guide
                                System.out.println();
                                System.out.println();
                                System.out.println("Welcome to the Expense Tracker CLI.");
                                System.out.println();

                                displayHelp();

                                while (true) {
                                        System.out.print("Enter CLI command ('help' for options): ");
                                        String input = scanner.nextLine();
                                        if ("exit".equalsIgnoreCase(input.trim())) {
                                                break;
                                        } else if ("help".equalsIgnoreCase(input.trim())) {
                                                displayHelp();
                                        } else {
                                                commandLine.execute(input.split("\\s+"));  // Execute the command
                                        }
                                }
                        }
                }).start();
        }

        private void displayHelp() {
                System.out.println("Available Commands: user, expense, budget, income, category");
                System.out.println("Available Options:  (-a, -u, -d, -l)");
                System.out.println();
                System.out.println("Format: <command> [option] Example user -a (Adds a user)");
                System.out.println();
                System.out.println("Use '-h' after a command for more info, e.g., 'user -h'.\n");
        }
}