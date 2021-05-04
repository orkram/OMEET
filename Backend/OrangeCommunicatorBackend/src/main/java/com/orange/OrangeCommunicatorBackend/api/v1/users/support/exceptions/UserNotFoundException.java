package com.orange.OrangeCommunicatorBackend.api.v1.users.support.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username){
        super(String.format("User with username %s not found", username));
    }
}
