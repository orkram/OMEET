package com.orange.OrangeCommunicatorBackend.api.v1.users.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserUpdateRequestBody {

    private final String e_mail;
    private final String first_name;
    private final String last_name;
    private final String img_url;


    @JsonCreator
    public UserUpdateRequestBody(String eMail, String firstName, String lastName,  String imgUrl) {
        this.e_mail = eMail;
        this.first_name = firstName;
        this.img_url = imgUrl;
        this.last_name = lastName;
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


    public String getImgURL() {
        return img_url;
    }


}
