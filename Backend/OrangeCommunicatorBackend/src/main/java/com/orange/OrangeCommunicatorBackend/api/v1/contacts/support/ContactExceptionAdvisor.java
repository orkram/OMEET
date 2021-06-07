package com.orange.OrangeCommunicatorBackend.api.v1.contacts.support;


import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.exceptions.EmailNotSentException;
import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.exceptions.FriendshipNotFoundException;
import com.orange.OrangeCommunicatorBackend.shared.responsesBody.ErrorMessageExceptionResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class ContactExceptionAdvisor {

    private static final Logger LOG = LoggerFactory.getLogger(ContactExceptionAdvisor.class);

    @ExceptionHandler(FriendshipNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageExceptionResponseBody friendshipNotFound(FriendshipNotFoundException exception) {
        LOG.error(exception.getMessage(), exception);
        return new ErrorMessageExceptionResponseBody(exception.getLocalizedMessage());
    }

    @ExceptionHandler(EmailNotSentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorMessageExceptionResponseBody emailNotSent(EmailNotSentException exception) {
        LOG.error(exception.getMessage(), exception);
        return new ErrorMessageExceptionResponseBody(exception.getLocalizedMessage());
    }

}
