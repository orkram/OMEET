package com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions;

public class AccountExistsException extends RuntimeException{
    public AccountExistsException() {
        super(String.format("Account with given username or e-mail already exists"));
    }

}
