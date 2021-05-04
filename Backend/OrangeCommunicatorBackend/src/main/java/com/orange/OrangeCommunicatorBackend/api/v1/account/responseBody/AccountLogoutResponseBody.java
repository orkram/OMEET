package com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountLogoutResponseBody {

    private final String successfulLogout;

    @JsonCreator
    public AccountLogoutResponseBody(String successfulLogout) {
        this.successfulLogout = successfulLogout;
    }

    public String getSuccessfulLogout() {
        return successfulLogout;
    }
}
