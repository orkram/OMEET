//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.contacts.support;

import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.exceptions.EmailNotSentException;
import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.exceptions.FriendshipNotFoundException;
import com.orange.OrangeCommunicatorBackend.dbEntities.FriendshipId;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;

import java.util.function.Supplier;

public class ContactExceptionSupplier {

    public static Supplier<FriendshipNotFoundException> friendshipNotFoundException(FriendshipId id){
        return () -> new FriendshipNotFoundException((id));
    }

    public static Supplier<EmailNotSentException> emailNotSentException(User user){
        return () -> new EmailNotSentException((user.getUserName()));
    }

    public static Supplier<EmailNotSentException> emailNotSentException(String username){
        return () -> new EmailNotSentException((username));
    }

}
