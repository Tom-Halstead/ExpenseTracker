package com.expensetracker.cli.commands;

import com.expensetracker.cli.events.UserLoginEvent;
import com.expensetracker.cli.events.UserLogoutEvent;
import com.expensetracker.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
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
                CategoryCommand.class,
                LogoutCommand.class,
                ExitCommand.class,
                CommandLine.HelpCommand.class
        }
)
public class MainCommand implements Runnable, ApplicationListener<ApplicationEvent> {
    private volatile boolean running = true;
    private UserDTO loggedInUser;
    private Map<String, Runnable> commandMap = new HashMap<>();


    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Scanner scanner;



    public MainCommand() {
    }

    @Override
    public void run() {
        while (running) {
            System.out.println("Enter command (manage incomes, manage expenses, manage budgets, manage categories, logout):");
            System.out.println();
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


    // I think there's a more intuitive way of doing this especially using PicoCLI, but I have already manually created the implementation, so I will keep it as it is.
    // This is for learning purposes so anything manual is *advantageous* to my advancement within programming
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

        ExitCommand exitCommand = applicationContext.getBean(ExitCommand.class);
        exitCommand.setLoggedInUser(loggedInUser);
        commandMap.put("exit", new ExitCommand(this));

        LogoutCommand logoutCommand = applicationContext.getBean(LogoutCommand.class);
        logoutCommand.setLoggedInUser(loggedInUser);
        commandMap.put("logout", logoutCommand);
    }




    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserLoginEvent) {
            this.loggedInUser = ((UserLoginEvent)event).getUser();
            initCommands();
            run();
        } else if (event instanceof UserLogoutEvent) {
            loggedInUser = null;
            stopRunning();
        }
    }

    private void clearBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    public void stopRunning() {
        this.running = false;
    }

    public UserDTO getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }


}