package com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountLogoutResponseBody {

    private final String successfulLogout;

    @JsonCreator
    public AccountLogoutResponseBody(@JsonProperty("successfulLogout") String successfulLogout) {
        this.successfulLogout = successfulLogout;
    }

    public String getSuccessfulLogout() {
        return successfulLogout;
    }
}
