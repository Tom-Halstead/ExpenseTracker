package com.expensetracker.cli.events;

import com.expensetracker.dto.UserDTO;
import org.springframework.context.ApplicationEvent;

public class UserLoginEvent extends ApplicationEvent {
    private final UserDTO user;

    public UserLoginEvent(Object source, UserDTO user) {
        super(source);
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }
}
