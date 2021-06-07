package com.orange.OrangeCommunicatorBackend.api.v1.meetings.support;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.NewMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.UpdateMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserMapper;
import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MeetingsMapper {

    private final UserMapper userMapper;

    public MeetingsMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
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

    public MeetingResponseBody toMeetingResponseBody(Meeting meeting) {
        MeetingResponseBody meetingResponseBody = new MeetingResponseBody(Long.toString(meeting.getIdMeeting()),
                meeting.getName(), meeting.getSqlTimestamp(), meeting.getRoomUrl(),
                userMapper.toUserResponseBody(meeting.getUser()));
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
