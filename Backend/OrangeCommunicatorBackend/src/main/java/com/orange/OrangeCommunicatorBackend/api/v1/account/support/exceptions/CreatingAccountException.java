//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions;

public class CreatingAccountException extends RuntimeException{
    public CreatingAccountException() {
        super(String.format("There was an error while creating new account"));
    }
}
