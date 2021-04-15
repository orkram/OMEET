package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountRegisterRequestBody {
    private final String username;
    private final String e_mail;
    private final String first_name;
    private final String last_name;
    private final String img_url;
    private final String password;

    @JsonCreator
    public AccountRegisterRequestBody(String userName, String eMail, String firstName, String lastName, String imgURL, String password) {
        this.username = userName;
        this.e_mail = eMail;
        this.first_name = firstName;
        this.last_name = lastName;
        this.img_url = imgURL;
        this.password = password;
    }

    public String geteMail() {
        return e_mail;
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getPassword() {
        return password;
    }

    public String getImgURL() {
        return img_url;
    }

    public String getUserName() {
        return username;
    }
}
