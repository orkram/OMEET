package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountRefreshTokenRequestBody {

    private final String client_id;
    private final String refresh_token;
    private final String client_secret;

    @JsonCreator
    public AccountRefreshTokenRequestBody(String client_id, String refresh_token, String client_secret) {
        this.client_id = client_id;
        this.refresh_token = refresh_token;
        this.client_secret = client_secret;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getClient_secret() {
        return client_secret;
    }
}
