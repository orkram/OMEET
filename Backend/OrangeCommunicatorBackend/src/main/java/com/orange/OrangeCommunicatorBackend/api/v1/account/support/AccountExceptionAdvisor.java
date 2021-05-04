package com.orange.OrangeCommunicatorBackend.api.v1.account.support;


import com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions.AccountExistsException;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions.CreatingAccountException;
import com.orange.OrangeCommunicatorBackend.api.v1.account.support.exceptions.TokenAcquireException;
import com.orange.OrangeCommunicatorBackend.shared.responsesBody.ErrorMessageExceptionResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AccountExceptionAdvisor {

    private static final Logger LOG = LoggerFactory.getLogger(AccountExceptionAdvisor.class);

    @ExceptionHandler(AccountExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorMessageExceptionResponseBody accountExists(AccountExistsException exception) {
        LOG.error(exception.getMessage(), exception);
        return new ErrorMessageExceptionResponseBody(exception.getLocalizedMessage());
    }

    @ExceptionHandler(CreatingAccountException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageExceptionResponseBody creatingAccountError(CreatingAccountException exception) {
        LOG.error(exception.getMessage(), exception);
        return new ErrorMessageExceptionResponseBody(exception.getLocalizedMessage());
    }

    @ExceptionHandler(TokenAcquireException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessageExceptionResponseBody tokenAcquire(TokenAcquireException exception) {
        LOG.error(exception.getMessage(), exception);
        return new ErrorMessageExceptionResponseBody(exception.getLocalizedMessage());
    }

}
