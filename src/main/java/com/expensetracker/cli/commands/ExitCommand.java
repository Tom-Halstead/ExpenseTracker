package com.expensetracker.cli.commands;

import com.expensetracker.cli.commands.interfaces.UserAwareCommand;
import com.expensetracker.dto.UserDTO;
import org.springframework.stereotype.Component;
import picocli.CommandLine;


@Component
@CommandLine.Command(name = "exit", description = "Command for exiting current execution flow.")
public class ExitCommand implements UserAwareCommand {
    private MainCommand mainCommand;

    private UserDTO loggedInUser;

    public ExitCommand(MainCommand mainCommand) {
        this.mainCommand = mainCommand;
    }
    @Override
    public void run() {

        mainCommand.stopRunning();
        System.out.println("Exiting...");
    }


    @Override
    public void setLoggedInUser(UserDTO user) {
    }
}
