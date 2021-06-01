package com.orange.OrangeCommunicatorBackend.api.v1.users.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserUpdateRequestBody {

    private String eMail;
    private String firstName;
    private String lastName;
    private String imgUrl;


    @JsonCreator
    public UserUpdateRequestBody(@JsonProperty("eMail") String eMail,
                                 @JsonProperty("firstName") String firstName,
                                 @JsonProperty("lastName") String lastName,
                                 @JsonProperty("imgUrl") String imgUrl) {
        this.eMail = eMail;
        this.firstName = firstName;
        this.imgUrl = imgUrl;
        this.lastName = lastName;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
