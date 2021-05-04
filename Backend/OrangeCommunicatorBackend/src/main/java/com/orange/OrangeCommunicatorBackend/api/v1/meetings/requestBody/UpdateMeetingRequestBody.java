package com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;

public class UpdateMeetingRequestBody {

    private final String name;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
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
