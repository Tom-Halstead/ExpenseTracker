package com.expensetracker;

import com.expensetracker.cli.commands.MainCommand;
import com.expensetracker.cli.commands.UserCommand;
import com.expensetracker.config.SpringPicocliFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import picocli.CommandLine;

import java.util.Scanner;

@SpringBootApplication
public class ExpenseTrackerApp {

        @Autowired
        private ApplicationContext applicationContext;

        @Autowired
        private Scanner scanner;

        public static void main(String[] args) {
                SpringApplication.run(ExpenseTrackerApp.class, args);
        }

        @Bean
        public CommandLine.IFactory picocliFactory() {
                return new SpringPicocliFactory(applicationContext);
        }

        @Bean
        public CommandLine commandLine() {
                MainCommand mainCommand = applicationContext.getBean(MainCommand.class);
                // Since subcommands are added via annotations in MainCommand, no need to add again here.
                return new CommandLine(mainCommand, picocliFactory());
        }



        @EventListener(ApplicationReadyEvent.class)
        public void runCLI() {
                CommandLine commandLine = applicationContext.getBean(CommandLine.class);
                System.out.println();
                System.out.println("Welcome to the Expense Tracker CLI.");
                System.out.println();
                System.out.println("Type 'user login' or 'user register' to start.");
                System.out.println();

                new Thread(() -> {
                        while (true) {
                                System.out.print("Enter CLI command ('help' for options): ");
                                String input = scanner.nextLine().trim();
                                if ("exit".equalsIgnoreCase(input)) {
                                        System.out.println("Exiting the application...");
                                        break;
                                } else {
                                        commandLine.execute(input.split("\\s+"));  // Execute commands split by spaces
                                }
                        }
                        scanner.close();
                }).start();
        }
}