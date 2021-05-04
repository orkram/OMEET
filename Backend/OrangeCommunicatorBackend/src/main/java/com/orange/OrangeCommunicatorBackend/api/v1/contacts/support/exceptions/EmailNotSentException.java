package com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.exceptions;

import com.orange.OrangeCommunicatorBackend.dbEntities.User;

public class EmailNotSentException extends RuntimeException{
    public EmailNotSentException(User user){
        super(String.format("Email to user %s has not been sent. Probably email was invalid.", user.getUserName()));
    }
}
