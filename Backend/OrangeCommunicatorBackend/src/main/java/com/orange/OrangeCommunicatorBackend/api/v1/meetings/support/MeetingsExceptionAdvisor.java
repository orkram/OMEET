package com.orange.OrangeCommunicatorBackend.api.v1.meetings.support;


import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.exceptions.CreatingMeetingErrorException;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.exceptions.MeetingNotFoundException;
import com.orange.OrangeCommunicatorBackend.shared.responsesBody.ErrorMessageExceptionResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MeetingsExceptionAdvisor {

    private static final Logger LOG = LoggerFactory.getLogger(MeetingsExceptionAdvisor.class);

    @ExceptionHandler(MeetingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageExceptionResponseBody meetingNotFound(MeetingNotFoundException exception) {
        LOG.error(exception.getMessage(), exception);
        return new ErrorMessageExceptionResponseBody(exception.getLocalizedMessage());
    }

    @ExceptionHandler(CreatingMeetingErrorException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageExceptionResponseBody creatingMeetingError(CreatingMeetingErrorException exception) {
        LOG.error(exception.getMessage(), exception);
        return new ErrorMessageExceptionResponseBody(exception.getLocalizedMessage());
    }

}
