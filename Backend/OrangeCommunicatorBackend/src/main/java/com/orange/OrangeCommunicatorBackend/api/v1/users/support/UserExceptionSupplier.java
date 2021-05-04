package com.orange.OrangeCommunicatorBackend.api.v1.users.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.support.exceptions.UserNotFoundException;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;

import java.util.function.Supplier;

public class UserExceptionSupplier {

    public static Supplier<UserNotFoundException> userNotFoundException(String username) {
        return () -> new UserNotFoundException(username);
    }

    public static Supplier<UserNotFoundException> userNotFoundException(User user) {
        return () -> new UserNotFoundException(user.getUserName());
    }
}
