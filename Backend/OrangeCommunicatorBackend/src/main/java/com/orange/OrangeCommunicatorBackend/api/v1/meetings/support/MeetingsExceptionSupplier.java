//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.meetings.support;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.exceptions.CreatingMeetingErrorException;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.exceptions.MeetingNotFoundException;
import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;

import java.util.function.Supplier;

public class MeetingsExceptionSupplier {

    public static Supplier<MeetingNotFoundException> meetingNotFoundException(Meeting meeting) {
        return () -> new MeetingNotFoundException(meeting.getIdMeeting());
    }

    public static Supplier<MeetingNotFoundException> meetingNotFoundException(long id) {
        return () -> new MeetingNotFoundException(id);
    }

    public static Supplier<CreatingMeetingErrorException> creatingMeetingErrorException() {
        return () -> new CreatingMeetingErrorException();
    }
}
