package com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;

import java.sql.Timestamp;

public class MeetingResponseBody {

    private final long idMeeting;
    private final String name;
    private final java.sql.Timestamp date;
    private final String roomUrl;
    private final UserResponseBody owner;

    @JsonCreator
    public MeetingResponseBody(long idMeeting, String name, Timestamp date, String roomUrl, UserResponseBody owner) {
        this.idMeeting = idMeeting;
        this.name = name;
        this.date = date;
        this.roomUrl = roomUrl;
        this.owner = owner;
    }

    public long getIdMeeting() {
        return idMeeting;
    }

    public String getName() {
        return name;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public UserResponseBody getOwner() {
        return owner;
    }
}
