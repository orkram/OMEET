package com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.sql.Timestamp;
import java.util.List;

public class UpdateMeetingRequestBody {

    private final String name;
    private final java.sql.Timestamp date;
    private final String ownerUserName;

    @JsonCreator
    public UpdateMeetingRequestBody(String name, Timestamp date, String ownerUserName) {
        this.name = name;
        this.date = date;
        this.ownerUserName = ownerUserName;
    }

    public String getName() {
        return name;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }
}
