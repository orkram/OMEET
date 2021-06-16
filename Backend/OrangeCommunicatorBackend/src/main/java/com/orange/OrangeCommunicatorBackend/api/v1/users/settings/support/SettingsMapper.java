//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.users.settings.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.requestBody.UpdateSettingsRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.responseBody.SettingsResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.Settings;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;


@Component
public class SettingsMapper {

    public SettingsResponseBody toSettingsResponseBody(Settings settings){

        return new SettingsResponseBody(settings.getUserName(), settings.isPrivate(),
                settings.isDefaultMicOn(), settings.isDefaultCamOn());
    }

    public Settings toSettings(Settings settings, UpdateSettingsRequestBody updateSettingsRequestBody){
        settings.setDefaultMicOn(updateSettingsRequestBody.isDefaultMicOn());
        settings.setDefaultCamOn(updateSettingsRequestBody.isDefaultCamOn());
        settings.setPrivate(updateSettingsRequestBody.isPrivate());
        return settings;
    }

    public Settings createDefaultSettings(User user){
        Settings settings = new Settings();
        settings.setUser(user);
        settings.setPrivate(false);
        settings.setDefaultMicOn(false);
        settings.setDefaultCamOn(false);
        return settings;
    }

}
