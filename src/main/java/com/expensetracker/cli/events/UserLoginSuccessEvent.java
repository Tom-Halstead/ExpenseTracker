package com.expensetracker.cli.events;

import com.expensetracker.dto.UserDTO;
import org.springframework.context.ApplicationEvent;

public class UserLoginSuccessEvent extends ApplicationEvent {
    private final UserDTO user;

    public UserLoginSuccessEvent(Object source, UserDTO user) {
        super(source);
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }
}
