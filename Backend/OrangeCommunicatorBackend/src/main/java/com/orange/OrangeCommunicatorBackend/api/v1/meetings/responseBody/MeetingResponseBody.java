package com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;

import java.sql.Timestamp;

public class MeetingResponseBody {

    private final String idMeeting;
    private final String name;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private final java.sql.Timestamp date;
    private final String roomUrl;
    private final UserResponseBody owner;

    @JsonCreator
    public MeetingResponseBody(@JsonProperty("idMeeting") String idMeeting,
                               @JsonProperty("name") String name,
                               @JsonProperty("date") Timestamp date,
                               @JsonProperty("roomUrl") String roomUrl,
                               @JsonProperty("owner") UserResponseBody owner) {
        this.idMeeting = idMeeting;
        this.name = name;
        this.date = date;
        this.roomUrl = roomUrl;
        this.owner = owner;
    }

    public String getIdMeeting() {
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
