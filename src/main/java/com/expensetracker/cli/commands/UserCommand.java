package com.expensetracker.cli.commands;

import com.expensetracker.dto.RegistrationResult;
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

        // Create a new UserDTO with all provided fields
        UserDTO newUserDTO = new UserDTO(null, username, email, firstName, lastName, true, password);

        // Call the service to register the user
        RegistrationResult registrationResult = userService.addUser(newUserDTO);

        if (registrationResult != null && "SUCCESS".equals(registrationResult.getStatus())) {
            System.out.println();
            System.out.println("Registration successful for: " + username);

            // Use fetchUserDTOFromResult to retrieve the logged-in user details
            UserDTO loggedInUser = userService.fetchUserDTOFromResult(registrationResult);
            if (loggedInUser != null) {
                System.out.println();
                System.out.println("Logged-in User Details:");
                System.out.println("Username: " + loggedInUser.getUsername());
                System.out.println("Email: " + loggedInUser.getEmail());
                System.out.println("First Name: " + loggedInUser.getFirstName());
                System.out.println("Last Name: " + loggedInUser.getLastName());
                System.out.println();
                runPostLoginOptions(loggedInUser); // Proceed with post-login options
            } else {
                System.out.println();
                System.out.println("Error: Could not retrieve user details post-registration.");
            }
        } else if (registrationResult != null && "ERROR".equals(registrationResult.getStatus())
                && registrationResult.getMessage().contains("User already exists")) {
            System.out.println();
            System.out.println("Registration failed: User already exists.");
        } else {
            System.out.println();
            System.out.println("Registration failed: "
                    + (registrationResult != null ? registrationResult.getMessage() : "Unknown error."));
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
            runPostLoginOptions(userService.findByUsername(loggedInUser.getUsername()));
        } else {
            System.out.println("Login failed. Please check your credentials.");
        }
    }

    private void runPostLoginOptions(UserDTO loggedInUser) {
        boolean running = true;
        while (running) {
            System.out.println("Enter command (manage incomes, manage expenses, manage budgets, manage categories, logout): ");
            String command = scanner.nextLine();
            switch (command.toLowerCase()) {
                case "manage incomes":
                    // Implement income functionality here
                    System.out.println("Incomes..."); // Placeholder
                    break;
                case "manage expenses":
                    // Implement expense functionality here
                    System.out.println("Expenses..."); // Placeholder
                    break;
                case "manage budgets":
                    // Implement budget functionality here
                    System.out.println("Budgets..."); // Placeholder
                    break;
                case "manage categories":
                    // Implement category functionality here
                    System.out.println("Categories...");
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
