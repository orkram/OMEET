package com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountLogoutResponseBody {

    private final String successful_logout;

    @JsonCreator
    public AccountLogoutResponseBody(String successfulLogout) {
        this.successful_logout = successfulLogout;
    }

    public String getSuccessfulLogout() {
        return successful_logout;
    }
}
