//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.support;

import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.MeetingUserList;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;

@Component
public class ParticipantsMapper {

    public MeetingUserList toMeetingParticipant(Meeting meeting, User user) {
        MeetingUserList meetingUserList = new MeetingUserList();
        meetingUserList.setMeeting(meeting);
        meetingUserList.setUser(user);
        return meetingUserList;
    }

}
