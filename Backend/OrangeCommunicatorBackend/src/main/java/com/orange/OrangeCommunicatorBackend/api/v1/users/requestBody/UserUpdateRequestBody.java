package com.orange.OrangeCommunicatorBackend.api.v1.users.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserUpdateRequestBody {

    private final String eMail;
    private final String firstName;
    private final String lastName;
    private final String imgUrl;


    @JsonCreator
    public UserUpdateRequestBody(String eMail, String firstName, String lastName,  String imgUrl) {
        this.eMail = eMail;
        this.firstName = firstName;
        this.imgUrl = imgUrl;
        this.lastName = lastName;
    }

    public String getEMail() {
        return eMail;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getImgURL() {
        return imgUrl;
    }


}
