package com.orange.OrangeCommunicatorBackend.dbEntities;


import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity(name="Meetings")
@Data
public class Meeting {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long idMeeting;

    @Column(name="time", nullable=true)
    private java.sql.Timestamp sqlTimestamp;

    @Column(name="room_url", nullable=false)
    private String roomUrl;



    @ManyToOne
    @JoinColumn(name="owner")
    private User user;

    @OneToMany(mappedBy = "meeting")
    private Set<MeetingUserList> meetingUserList;

}
