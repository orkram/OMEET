package com.orange.OrangeCommunicatorBackend.dbEntities;


import lombok.Data;

import javax.persistence.*;

@Entity(name = "MeetingUserList")
@IdClass(ParticipantId.class)
@Data
public class MeetingUserList {

    @Id
    private long id_meeting;

    @Id
    private String user_name;



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
