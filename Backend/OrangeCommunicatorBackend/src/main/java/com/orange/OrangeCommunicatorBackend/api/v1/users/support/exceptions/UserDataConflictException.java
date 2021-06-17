//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.users.support.exceptions;

public class UserDataConflictException extends RuntimeException {
    public UserDataConflictException(String username){
        super(String.format("Data conflict occurred while updating user %s", username));
    }
}