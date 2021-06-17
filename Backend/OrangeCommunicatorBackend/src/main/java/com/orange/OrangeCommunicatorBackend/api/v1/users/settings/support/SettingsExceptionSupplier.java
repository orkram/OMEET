//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.users.settings.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.support.exceptions.SettingsNotFoundException;

import java.util.function.Supplier;

public class SettingsExceptionSupplier {

    public static Supplier<SettingsNotFoundException> settingsNotFound(String username) {
        return () -> new SettingsNotFoundException(username);
    }

}
