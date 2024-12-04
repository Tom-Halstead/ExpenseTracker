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
    private volatile boolean running = true;
    private UserDTO loggedInUser;
    private Map<String, Runnable> commandMap = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);

    @Autowired
    private ApplicationContext applicationContext;



    public MainCommand() {
    }

    @Override
    public void run() {
        while (running) {
            System.out.println("Enter command:");
            String input = scanner.nextLine().trim().toLowerCase();
            Runnable command = commandMap.get(input);
            if (command != null) {
                command.run();
                clearBuffer(scanner);
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }
    }

    private void initCommands() {
        IncomeCommand incomeCommand = applicationContext.getBean(IncomeCommand.class);
        incomeCommand.setLoggedInUser(loggedInUser);
        commandMap.put("manage incomes", incomeCommand);

        ExpenseCommand expenseCommand = applicationContext.getBean(ExpenseCommand.class);
        expenseCommand.setLoggedInUser(loggedInUser);
        commandMap.put("manage expenses", expenseCommand);

        BudgetCommand budgetCommand = applicationContext.getBean(BudgetCommand.class);
        budgetCommand.setLoggedInUser(loggedInUser);
        commandMap.put("manage budgets", budgetCommand);

        CategoryCommand categoryCommand = applicationContext.getBean(CategoryCommand.class);
        categoryCommand.setLoggedInUser(loggedInUser);
        commandMap.put("manage categories", categoryCommand);

        LogoutCommand logoutCommand = applicationContext.getBean(LogoutCommand.class);
        logoutCommand.setLoggedInUser(loggedInUser);
        commandMap.put("logout", logoutCommand);
    }

    @Override
    public void onApplicationEvent(UserLoginSuccessEvent event) {
        this.loggedInUser = event.getUser();
        initCommands();
        run();
    }

    private void clearBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

}