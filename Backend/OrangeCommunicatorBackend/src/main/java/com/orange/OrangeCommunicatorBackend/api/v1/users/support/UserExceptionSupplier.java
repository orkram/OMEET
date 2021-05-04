package com.orange.OrangeCommunicatorBackend.api.v1.users.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.support.exceptions.UserNotFoundException;

import java.util.function.Supplier;

public class UserExceptionSupplier {

    public static Supplier<UserNotFoundException> userNotFoundException(String username) {
        return () -> new UserNotFoundException(username);
    }
}
