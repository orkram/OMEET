package com.orange.OrangeCommunicatorBackend.dbEntities;


import lombok.Data;

import javax.persistence.*;

@Entity(name = "MeetingUserList")
@IdClass(ParticipantId.class)
@Data
public class MeetingUserList {

    @Id
    @Column(name = "id_meeting")
    private long idMeeting;

    @Id
    @Column(name = "user_name")
    private String userName;



    @ManyToOne
    @JoinColumn(name = "id_meeting")
    @MapsId
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "user_name")
    @MapsId
    private User user;

    MeetingUserList() {

    }

}
