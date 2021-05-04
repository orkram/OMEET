package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountLoginRequestBody {
    private final String clientId;
    private final String username;
    private final String password;
    private final String clientSecret;

    @JsonCreator
    public AccountLoginRequestBody(String clientId, String username, String password, String clientSecret) {
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.clientSecret = clientSecret;
    }

    public String getClient_id() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClient_secret() {
        return clientSecret;
    }
}
