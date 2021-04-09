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
    private long id_user;



    @ManyToOne
    @JoinColumn(name = "id_meeting")
    @MapsId
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "id_user")
    @MapsId
    private User user;

    MeetingUserList() {

    }

}
