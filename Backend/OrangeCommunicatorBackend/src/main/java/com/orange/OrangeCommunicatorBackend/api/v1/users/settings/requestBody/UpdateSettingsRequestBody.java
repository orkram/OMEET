//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.users.settings.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

public class UpdateSettingsRequestBody {

    private final boolean isPrivate;
    private final boolean isDefaultMicOn;
    private final boolean isDefaultCamOn;

    @JsonCreator
    public UpdateSettingsRequestBody(@JsonProperty("isPrivate") boolean isPrivate,
                                     @JsonProperty("isDefaultMicOn")boolean isDefaultMicOn,
                                     @JsonProperty("isDefaultCamOn") boolean isDefaultCamOn) {
        this.isPrivate = isPrivate;
        this.isDefaultMicOn = isDefaultMicOn;
        this.isDefaultCamOn = isDefaultCamOn;
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
