//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.exceptions;

import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;

public class MeetingNotFoundException extends RuntimeException {
    public MeetingNotFoundException(long id) {
        super(String.format("Meeting with id %d not found", id));
    }
}
