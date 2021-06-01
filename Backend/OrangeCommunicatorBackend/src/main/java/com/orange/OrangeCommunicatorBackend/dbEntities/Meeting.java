package com.orange.OrangeCommunicatorBackend.dbEntities;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity(name="Meetings")
@Data
public class Meeting {

    @Id
    private long idMeeting;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="time", nullable=true)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private java.sql.Timestamp sqlTimestamp;

    @Column(name="room_url", nullable=false)
    private String roomUrl;


    @ManyToOne
    @JoinColumn(name="owner")
    private User user;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MeetingUserList> meetingUserList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getSqlTimestamp() {
        return sqlTimestamp;
    }

    public void setSqlTimestamp(Timestamp sqlTimestamp) {
        this.sqlTimestamp = sqlTimestamp;
    }

    public String getRoomUrl() {
        return roomUrl;
    }

    public void setRoomUrl(String roomUrl) {
        this.roomUrl = roomUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
