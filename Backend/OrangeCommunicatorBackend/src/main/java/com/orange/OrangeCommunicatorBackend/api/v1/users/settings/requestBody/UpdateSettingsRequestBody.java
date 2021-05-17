package com.orange.OrangeCommunicatorBackend.api.v1.users.settings.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.Column;

public class UpdateSettingsRequestBody {

    private final boolean isPrivate;
    private final boolean isDefaultMicOn;
    private final boolean isDefaultCamOn;

    @JsonCreator
    public UpdateSettingsRequestBody(boolean isPrivate, boolean isDefaultMicOn, boolean isDefaultCamOn) {
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
