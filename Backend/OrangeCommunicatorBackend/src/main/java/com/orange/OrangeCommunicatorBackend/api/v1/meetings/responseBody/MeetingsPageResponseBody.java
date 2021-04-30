package com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;

import java.util.List;

public class MeetingsPageResponseBody {

    private final long allFoundMeetings;
    private final long allFoundPages;
    private final List<MeetingResponseBody> found_meetings;

    @JsonCreator
    public MeetingsPageResponseBody(long allFoundMeetings, long allFoundPages,
                                    List<MeetingResponseBody> found_meetings) {
        this.allFoundMeetings = allFoundMeetings;
        this.allFoundPages = allFoundPages;
        this.found_meetings = found_meetings;
    }

    public long getAll_found_users() {
        return allFoundMeetings;
    }

    public long getAll_found_pages() {
        return allFoundPages;
    }

    public List<MeetingResponseBody> getFound_meetings() {
        return found_meetings;
    }
}
