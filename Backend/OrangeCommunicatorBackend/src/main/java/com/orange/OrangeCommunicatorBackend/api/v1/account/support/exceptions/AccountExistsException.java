//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions;

public class AccountExistsException extends RuntimeException{
    public AccountExistsException() {
        super(String.format("Account with given username or e-mail already exists"));
    }

}
