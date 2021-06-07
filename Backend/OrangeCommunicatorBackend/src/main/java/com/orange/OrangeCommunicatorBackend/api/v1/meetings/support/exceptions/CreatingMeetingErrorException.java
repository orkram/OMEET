package com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.exceptions;

public class CreatingMeetingErrorException extends RuntimeException{
    public CreatingMeetingErrorException() {
        super("An error occurred while creating the meeting. Try again.");
    }
}
