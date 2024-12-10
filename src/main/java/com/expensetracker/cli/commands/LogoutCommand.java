package com.expensetracker.cli.commands;

import com.expensetracker.cli.commands.interfaces.UserAwareCommand;
import com.expensetracker.dto.UserDTO;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "logout", description = "Commands related to logging out a user out.")
public class LogoutCommand implements UserAwareCommand {
    private UserDTO loggedInUser;

    @Autowired
    private MainCommand mainCommand;
    @Autowired
    private UserCommand userCommand;

    public LogoutCommand(MainCommand mainCommand) {
    this.mainCommand = mainCommand;
    }

    @Override
    public void run() {

        System.out.println("Logging out " + loggedInUser.getUsername() + "...");
        System.out.println();
        // Clear state
        loggedInUser = null;
        mainCommand.setLoggedInUser(null);
        mainCommand.stopRunning();
        System.out.println("Logged out.");

    }

    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
