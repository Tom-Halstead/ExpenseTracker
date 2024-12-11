package com.expensetracker.cli.commands;

import com.expensetracker.cli.commands.interfaces.UserAwareCommand;
import com.expensetracker.cli.events.UserLogoutEvent;
import com.expensetracker.dto.UserDTO;
import com.sun.tools.javac.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

@Component
@CommandLine.Command(name = "logout", description = "Commands related to logging out a user out.")
public class LogoutCommand implements UserAwareCommand {
    private UserDTO loggedInUser;

    @Autowired
    private MainCommand mainCommand;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public LogoutCommand(MainCommand mainCommand) {
    this.mainCommand = mainCommand;
    }


    // Using SpringBoot listeners and event publishers to de-couple code and handle login/logout events
    @Override
    public void run() {
        System.out.println();
        System.out.println("Logging out " + loggedInUser.getUsername() + "...");
        System.out.println();
        eventPublisher.publishEvent(new UserLogoutEvent(this));
        System.out.println("Logout successful for " + loggedInUser.getUsername());
    }




    public void setLoggedInUser(UserDTO loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
