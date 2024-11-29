package com.expensetracker.lambda;
import com.amazonaws.services.lambda.runtime.events.CognitoEvent;
import com.expensetracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.function.Function;

@Component
public class UserActivationFunction implements Function<CognitoEvent, String> {

    @Autowired
    private UserService userService;

    @Override
    public String apply(CognitoEvent event) {
        String cognitoUuid = event.getIdentityId(); // Ensure this is correct based on your Cognito setup.
        boolean activated = userService.activateUser(cognitoUuid);
        return activated ? "User activated successfully." : "User activation failed.";
    }
    }


