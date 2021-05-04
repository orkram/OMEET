package com.orange.OrangeCommunicatorBackend.api.v1.users.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.support.exceptions.UserNotFoundException;
import com.orange.OrangeCommunicatorBackend.shared.responsesBody.ErrorMessageExceptionResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionAdvisor {

    private static final Logger LOG = LoggerFactory.getLogger(UserExceptionAdvisor.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageExceptionResponseBody itemNotFound(UserNotFoundException exception) {
        LOG.error(exception.getMessage(), exception);
        return new ErrorMessageExceptionResponseBody(exception.getLocalizedMessage());
    }


}
