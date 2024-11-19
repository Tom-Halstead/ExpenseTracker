package com.expensetracker.cli.commands;

import com.expensetracker.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import com.expensetracker.service.UserService;

import java.util.Scanner;

@Component
@CommandLine.Command(name = "user", description = "Commands related to user management")
public class UserCommand implements Runnable {

    @Autowired
    private UserService userService;

    private UserDTO loggedInUser;

    @CommandLine.Option(names = {"-reg", "--register"}, description = "Register a new user")
    private boolean register;

    @CommandLine.Option(names = {"-login", "--login"}, description = "Log in as a user")
    private boolean login;

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        if (register) {
            registerUser();
        } else if (login) {
            loginUser();
        } else {
            System.out.println("Please specify an option: --register or --login to access other functionalities.");
        }
    }

    private void registerUser() {
        System.out.println("Please enter your registration details:");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        UserDTO newUserDTO = new UserDTO(null, username, email, firstName, lastName, true, password);
        UserDTO registeredUser = userService.addUser(newUserDTO);
        if (registeredUser != null) {
            System.out.println("Registration successful for: " + username);
            loggedInUser = registeredUser;
            runPostLoginOptions();
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }

    private void loginUser() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        loggedInUser = userService.login(email, password);
        if (loggedInUser != null) {
            System.out.println("Login successful for: " + email);
            runPostLoginOptions();
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    private void runPostLoginOptions() {
        System.out.println("Logged in as: " + loggedInUser.getUsername());
        boolean running = true;
        while (running) {
            System.out.println("Enter command (add, delete, list, logout): ");
            String command = scanner.nextLine();
            switch (command.toLowerCase()) {
                case "add":
                    // Implement add functionality here
                    System.out.println("Adding something..."); // Placeholder
                    break;
                case "delete":
                    // Implement delete functionality here
                    System.out.println("Deleting something..."); // Placeholder
                    break;
                case "list":
                    // Implement list functionality here
                    System.out.println("Listing items..."); // Placeholder
                    break;
                case "logout":
                    System.out.println("Logging out...");
                    loggedInUser = null;
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command. Try again.");
                    break;
            }
        }
    }
}
