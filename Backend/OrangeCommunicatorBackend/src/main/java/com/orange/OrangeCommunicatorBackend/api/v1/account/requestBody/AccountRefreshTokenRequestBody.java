package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountRefreshTokenRequestBody {

    private final String clientId;
    private final String refreshToken;
    private final String clientSecret;

    @JsonCreator
    public AccountRefreshTokenRequestBody(String clientId, String refreshToken, String clientSecret) {
        this.clientId = clientId;
        this.refreshToken = refreshToken;
        this.clientSecret = clientSecret;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
