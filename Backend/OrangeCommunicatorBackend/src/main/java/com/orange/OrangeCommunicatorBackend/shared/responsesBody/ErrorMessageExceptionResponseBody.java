package com.orange.OrangeCommunicatorBackend.shared.responsesBody;

public class ErrorMessageExceptionResponseBody {

    private final String message;

    public ErrorMessageExceptionResponseBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
