package com.expensetracker.cli.commands.interfaces;

import com.expensetracker.dto.UserDTO;

public interface UserAwareCommand extends Runnable {
    void setLoggedInUser(UserDTO user);

}
