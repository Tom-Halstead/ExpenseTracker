package com.expensetracker.cli.commands;

import com.expensetracker.cli.events.UserLoginSuccessEvent;
import com.expensetracker.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
public class MainCommand implements Runnable, ApplicationListener<UserLoginSuccessEvent> {
    private UserDTO loggedInUser;
    private Map<String, Command> commandMap = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    @Autowired
    private ApplicationContext applicationContext;



    public MainCommand() {
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("Enter command:");
            String input = scanner.nextLine().toLowerCase();
            Command command = commandMap.get(input);
            if (command != null) {
                command.execute();
                if ("logout".equals(input)) {
                    running = false;
                }
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }
    }

    private void initCommands() {
        commandMap.put("manage incomes", new IncomeCommand(loggedInUser));
        commandMap.put("manage expenses", new ExpenseCommand(loggedInUser));
        commandMap.put("manage budgets", new BudgetCommand(loggedInUser));
        commandMap.put("manage categories", new CategoryCommand(loggedInUser));
        commandMap.put("logout", new LogoutCommand(loggedInUser));
    }

    @Override
    public void onApplicationEvent(UserLoginSuccessEvent event) {
        this.loggedInUser = event.getUser();
        initCommands();
        run();
    }

}