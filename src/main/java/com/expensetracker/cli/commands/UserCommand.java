package com.expensetracker.cli.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "user", description = "Commands related to user management")
public class UserCommand {

    @CommandLine.Option(names = {"-a", "--add"}, description = "Add a new user")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete a user")
    private boolean delete;

    public void run() {
        if (add) {
            System.out.println("Adding a new user...");
            // Implementation to add user
        } else if (delete) {
            System.out.println("Deleting a user...");
            // Implementation to delete user
        } else {
            System.out.println("Invalid option");
        }
    }
}