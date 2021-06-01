package com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.List;

public class NewMeetingRequestBody {

    private String name;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp date;
    private String ownerUserName;
    private List<String> participants;

    @JsonCreator
    public NewMeetingRequestBody(@JsonProperty("name") String name,
                                 @JsonProperty("date") Timestamp date,
                                 @JsonProperty("ownerUserName") String ownerUserName,
                                 @JsonProperty("participants") List<String> participants) {
        this.name = name;
        this.date = date;
        this.ownerUserName = ownerUserName;
        this.participants = participants;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public void setParticipants(List<String> participants) {
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
