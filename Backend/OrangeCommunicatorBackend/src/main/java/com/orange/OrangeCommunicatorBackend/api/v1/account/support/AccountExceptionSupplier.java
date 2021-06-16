//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.account.support;

import com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions.AccountExistsException;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions.CreatingAccountException;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions.TokenAcquireException;

import java.util.function.Supplier;

public class AccountExceptionSupplier {

    public static Supplier<AccountExistsException> accountExistsException(){
        return () -> new AccountExistsException();
    }

    public static Supplier<CreatingAccountException> creatingAccountException() {
        return () -> new CreatingAccountException();
    }

    public static Supplier<TokenAcquireException> tokenAcquireException() {
        return () -> new TokenAcquireException();
    }

}
