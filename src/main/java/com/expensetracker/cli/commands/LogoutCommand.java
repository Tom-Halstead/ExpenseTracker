package com.expensetracker.cli.commands;

import com.expensetracker.dto.UserDTO;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "logout", description = "Commands related to logging out a user out.")
public class LogoutCommand implements Runnable {
    private UserDTO loggedInUser;

    public LogoutCommand() {

    }

    @Override
    public void run() {

        System.out.println("Logging out " + loggedInUser.getUsername());
        // Perform cleanup, if necessary
        loggedInUser = null;
    }

    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
