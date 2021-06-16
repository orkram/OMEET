//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.users.settings.support.exceptions;

public class SettingsNotFoundException extends RuntimeException {

    public SettingsNotFoundException(String username) {
        super(String.format("Settings of user %s not found.", username));
    }
}
