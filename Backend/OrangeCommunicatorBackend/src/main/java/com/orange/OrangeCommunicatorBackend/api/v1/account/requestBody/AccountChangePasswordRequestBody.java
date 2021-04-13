package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountChangePasswordRequestBody {


    private final String password;

    @JsonCreator
    public AccountChangePasswordRequestBody(String password) {

        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
