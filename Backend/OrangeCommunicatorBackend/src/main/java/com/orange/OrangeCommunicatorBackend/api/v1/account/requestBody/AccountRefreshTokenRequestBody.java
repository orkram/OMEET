package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountRefreshTokenRequestBody {

    private final String refreshToken;

    @JsonCreator
    public AccountRefreshTokenRequestBody(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
