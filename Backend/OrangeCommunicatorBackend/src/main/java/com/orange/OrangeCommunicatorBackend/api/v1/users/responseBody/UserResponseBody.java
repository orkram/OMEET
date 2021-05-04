package com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserResponseBody {
    private final String username;
    private final String eMail;
    private final String firstName;
    private final String lastName;
    private final String imgUrl;


    @JsonCreator
    public UserResponseBody(String userName, String eMail, String firstName, String lastName,  String imgUrl) {
        this.username = userName;
        this.eMail = eMail;
        this.firstName = firstName;
        this.imgUrl = imgUrl;
        this.lastName = lastName;
    }

    public String geteMail() { return eMail; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return username;
    }

    public String getImgURL() {
        return imgUrl;
    }
}
