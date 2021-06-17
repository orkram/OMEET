//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
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
