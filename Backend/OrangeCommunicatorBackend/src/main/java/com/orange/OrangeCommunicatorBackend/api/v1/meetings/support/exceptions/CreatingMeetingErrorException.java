//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.exceptions;

public class CreatingMeetingErrorException extends RuntimeException{
    public CreatingMeetingErrorException() {
        super("An error occurred while creating the meeting. Try again.");
    }
}
