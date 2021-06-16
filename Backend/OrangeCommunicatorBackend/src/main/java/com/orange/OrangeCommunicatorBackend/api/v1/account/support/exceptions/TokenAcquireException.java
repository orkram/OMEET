//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions;

public class TokenAcquireException extends RuntimeException{

    public TokenAcquireException() {
        super("The token could not be obtained");
    }
}
