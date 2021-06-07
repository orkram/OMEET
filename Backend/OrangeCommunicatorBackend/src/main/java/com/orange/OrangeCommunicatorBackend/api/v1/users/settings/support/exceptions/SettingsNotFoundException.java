package com.orange.OrangeCommunicatorBackend.api.v1.users.settings.support.exceptions;

public class SettingsNotFoundException extends RuntimeException {

    public SettingsNotFoundException(String username) {
        super(String.format("Settings of user %s not found.", username));
    }
}
