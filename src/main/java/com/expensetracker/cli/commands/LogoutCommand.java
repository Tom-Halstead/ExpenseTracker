package com.expensetracker.cli.commands;

import com.expensetracker.dto.UserDTO;

public class LogoutCommand implements Command {
    private UserDTO loggedInUser;

    public LogoutCommand() {

    }

    @Override
    public void execute() {
        System.out.println("Logging out " + loggedInUser.getUsername());
        // Perform cleanup, if necessary
        loggedInUser = null;
    }

    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
