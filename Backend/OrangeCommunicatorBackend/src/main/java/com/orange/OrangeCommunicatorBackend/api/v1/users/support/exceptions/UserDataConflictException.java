package com.orange.OrangeCommunicatorBackend.api.v1.users.support.exceptions;

public class UserDataConflictException extends RuntimeException {
    public UserDataConflictException(String username){
        super(String.format("Data conflict occurred while updating user %s", username));
    }
}