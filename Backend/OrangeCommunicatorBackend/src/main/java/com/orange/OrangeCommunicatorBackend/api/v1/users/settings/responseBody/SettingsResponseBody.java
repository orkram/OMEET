//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.users.settings.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsResponseBody {

    private final String username;
    private final boolean isPrivate;
    private final boolean isDefaultMicOn;
    private final boolean isDefaultCamOn;

    @JsonCreator
    public SettingsResponseBody(@JsonProperty("username") String username,
                                @JsonProperty("isPrivate") boolean isPrivate,
                                @JsonProperty("isDefaultMicOn") boolean isDefaultMicOn,
                                @JsonProperty("isDefaultCamOn")boolean isDefaultCamOn) {
        this.isPrivate = isPrivate;
        this.isDefaultMicOn = isDefaultMicOn;
        this.isDefaultCamOn = isDefaultCamOn;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public boolean isDefaultMicOn() {
        return isDefaultMicOn;
    }

    public boolean isDefaultCamOn() {
        return isDefaultCamOn;
    }


}
