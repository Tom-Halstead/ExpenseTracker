package com.expensetracker.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class AppCommandLineRunner implements CommandLineRunner {
    @Autowired
    private CommandProcessor commandProcessor;

    @Override
    public void run(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Enter command (): ");
                String command = scanner.nextLine();
                if ("exit".equalsIgnoreCase(command)) {
                    break;
                }
                commandProcessor.processCommand(command);
            }
        }
    }
}