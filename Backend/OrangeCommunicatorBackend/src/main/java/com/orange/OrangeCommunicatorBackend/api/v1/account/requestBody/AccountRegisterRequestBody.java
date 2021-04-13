package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountRegisterRequestBody {
    private final String userName;
    private final String eMail;
    private final String firstName;
    private final String lastName;
    private final String imgURL;
    private final String password;

    @JsonCreator
    public AccountRegisterRequestBody(String userName, String eMail, String firstName, String lastName, String imgURL, String password) {
        this.userName = userName;
        this.eMail = eMail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imgURL = imgURL;
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
        return imgURL;
    }

    public String getUserName() {
        return userName;
    }
}
