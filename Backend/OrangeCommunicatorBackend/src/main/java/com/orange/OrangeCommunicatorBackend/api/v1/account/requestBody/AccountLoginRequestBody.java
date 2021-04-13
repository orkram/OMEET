package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountLoginRequestBody {
    private final String client_id;
    private final String  username;
    private final String password;
    private final String client_secret;

    @JsonCreator
    public AccountLoginRequestBody(String client_id, String username, String password, String client_secret) {
        this.client_id = client_id;
        this.username = username;
        this.password = password;
        this.client_secret = client_secret;
    }

    public String getClient_id() {
        return client_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClient_secret() {
        return client_secret;
    }
}
