//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
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

    public MeetingUserList() {

    }


    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
        this.idMeeting = meeting.getIdMeeting();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userName = user.getUserName();
    }
}
