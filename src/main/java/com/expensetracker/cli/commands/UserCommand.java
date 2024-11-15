package com.expensetracker.cli.commands;

import com.expensetracker.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;
import com.expensetracker.service.UserService;

import java.util.List;
import java.util.Scanner;

@Component
@CommandLine.Command(name = "user", description = "Commands related to user management")
public class UserCommand implements Runnable {

    @Autowired
    private UserService userService;

    @CommandLine.Option(names = {"-a", "--add"}, description = "Add a new user")
    private boolean add;

    @CommandLine.Option(names = {"-d", "--delete"}, description = "Delete a user")
    private boolean delete;

    @CommandLine.Option(names = {"-l", "--list"}, description = "List all users")
    private boolean list;

    private Scanner scanner = new Scanner(System.in);  // Scanner for user input

    @Override
    public void run() {
        if (add) {
            System.out.println("Adding a new user...");
            addUser();
        } else if (delete) {
            System.out.println("Deleting a user...");
            deleteUser();
        } else if (list) {
            System.out.println("Listing all users...");
            listUsers();
        } else {
            System.out.println("Please specify an option: --add, --delete, or --list");
        }
    }

    private void addUser() {
        boolean usernameExists = true;
        String username = "";

        while (usernameExists) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();

            // Check if the username already exists
            UserDTO existingUser = userService.getUserByUsername(username);
            if (existingUser != null) {
                System.out.println("Username already exists. Please try a different username.");
            } else {
                usernameExists = false; // Username is available
            }
        }

        System.out.println("Username '" + username + "' is available!");

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        boolean isActive = true; // Default to true

        // Create UserDTO here
        UserDTO userDTO = new UserDTO(username, email, firstName, lastName, isActive);

        // Save the new user
        userService.addUser(userDTO);

        System.out.println("User added: " + userDTO.toString());
    }

    private void deleteUser() {
        System.out.print("Delete by username or id? ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("username")) {
            System.out.print("Enter the username: ");
            String username = scanner.nextLine();
            userService.deleteUserByUsername(username);
            System.out.println("User deleted: Username = " + username);
        } else if (choice.equals("id")) {
            System.out.print("Enter the user ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            userService.deleteUserById(id);
            System.out.println("User deleted: ID = " + id);
        } else {
            System.out.println("Invalid choice. Use 'username' for username or 'id' for ID.");
        }
    }

    private void listUsers() {
        List<UserDTO> users = userService.getAllUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            users.forEach(user -> System.out.println(user.toString()));
        }
    }
}