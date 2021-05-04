package com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;

public class NewMeetingRequestBody {

    private final String name;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private final java.sql.Timestamp date;
    private final String ownerUserName;
    private final List<String> participants;

    @JsonCreator
    public NewMeetingRequestBody(String name, Timestamp date, String ownerUserName, List<String> participants) {
        this.name = name;
        this.date = date;
        this.ownerUserName = ownerUserName;
        this.participants = participants;
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

    public List<String> getParticipants() {
        return participants;
    }
}
