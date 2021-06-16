//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.meetings.support;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.NewMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.UpdateMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserSupport;
import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingsMapper {

    private final UserMapper userMapper;
    private final UserSupport userSupport;

    public MeetingsMapper(UserMapper userMapper, UserSupport userSupport) {
        this.userMapper = userMapper;
        this.userSupport = userSupport;
    }

    public Meeting toMeeting(long id, NewMeetingRequestBody newMeetingRequestBody, String link, User owner) {

        Meeting meeting = new Meeting();
        meeting.setIdMeeting(id);
        meeting.setName(newMeetingRequestBody.getName());
        meeting.setUser(owner);
        meeting.setRoomUrl(link);
        meeting.setSqlTimestamp(newMeetingRequestBody.getDate());

        return meeting;
    }

    public MeetingResponseBody toMeetingResponseBody(Meeting meeting, boolean isGettingAvatar) {
        MeetingResponseBody meetingResponseBody = new MeetingResponseBody(Long.toString(meeting.getIdMeeting()),
                meeting.getName(), meeting.getSqlTimestamp(), meeting.getRoomUrl(),
                userMapper.toUserResponseBody(meeting.getUser(),
                        userSupport.processAvatar(meeting.getUser(), isGettingAvatar)));
        System.out.println(userSupport.processAvatar(meeting.getUser(), isGettingAvatar));
        return  meetingResponseBody;
    }



    public Meeting toMeeting(Meeting meeting, UpdateMeetingRequestBody updateMeetingRequestBody, User newOwner) {
        meeting.setSqlTimestamp(updateMeetingRequestBody.getDate());
        meeting.setUser(newOwner);
        meeting.setName(updateMeetingRequestBody.getName());
        return  meeting;
    }

    public MeetingsPageResponseBody toMeetingsPageResponseBody(List<MeetingResponseBody> meetings, int totalPages, long totalElements) {
        return new MeetingsPageResponseBody(totalElements, totalPages, meetings);
    }
}
