//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.exceptions;

import com.orange.OrangeCommunicatorBackend.dbEntities.User;

public class EmailNotSentException extends RuntimeException{
    public EmailNotSentException(String username){
        super(String.format("Email to user %s has not been sent. Probably email was invalid.", username));
    }
}
