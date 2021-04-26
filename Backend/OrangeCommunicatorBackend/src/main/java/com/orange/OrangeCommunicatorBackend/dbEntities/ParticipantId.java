package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class ParticipantId implements Serializable {

    private long idMeeting;
    private String userName;

    public ParticipantId(long idMeeting, String userName) {
        this.idMeeting = idMeeting;
        this.userName = userName;
    }

    public long getIdMeeting() {
        return idMeeting;
    }

    public void setIdMeeting(long idMeeting) {
        this.idMeeting = idMeeting;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantId that = (ParticipantId) o;
        return idMeeting == that.idMeeting && userName.equals(that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMeeting, userName);
    }
}
