package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountRegisterRequestBody {
    private final String username;
    private final String eMail;
    private final String firstName;
    private final String lastName;
    private final String imgUrl;
    private final String password;

    @JsonCreator
    public AccountRegisterRequestBody(String userName, String eMail, String firstName, String lastName, String imgURL, String password) {
        this.username = userName;
        this.eMail = eMail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imgUrl = imgURL;
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getImgURL() {
        return imgUrl;
    }

    public String getUserName() {
        return username;
    }
}
