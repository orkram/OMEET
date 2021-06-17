//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;

import java.util.List;

public class MeetingsPageResponseBody {

    private final long allFoundMeetings;
    private final long allFoundPages;
    private final List<MeetingResponseBody> found_meetings;

    @JsonCreator
    public MeetingsPageResponseBody(@JsonProperty("allFoundMeetings") long allFoundMeetings,
                                    @JsonProperty("allFoundPages") long allFoundPages,
                                    @JsonProperty("found_meetings") List<MeetingResponseBody> found_meetings) {
        this.allFoundMeetings = allFoundMeetings;
        this.allFoundPages = allFoundPages;
        this.found_meetings = found_meetings;
    }

    public long getAllFoundUsers() {
        return allFoundMeetings;
    }

    public long getAllFoundPages() {
        return allFoundPages;
    }

    public List<MeetingResponseBody> getFoundMeetings() {
        return found_meetings;
    }
}
