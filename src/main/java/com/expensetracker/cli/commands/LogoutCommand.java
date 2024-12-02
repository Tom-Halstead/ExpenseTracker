package com.expensetracker.cli.commands;

import com.expensetracker.dto.UserDTO;

public class LogoutCommand implements Command {
    private UserDTO user;

    public LogoutCommand(UserDTO user) {
        this.user = user;
    }

    @Override
    public void execute() {
        System.out.println("Logging out " + user.getUsername());
        // Perform cleanup, if necessary
        user = null;
    }
}
