package com.expensetracker.cli.events;

import com.expensetracker.dto.UserDTO;
import org.springframework.context.ApplicationEvent;

public class UserLogoutEvent extends ApplicationEvent {
    public UserLogoutEvent(Object source) {
        super(source);
    }

}
