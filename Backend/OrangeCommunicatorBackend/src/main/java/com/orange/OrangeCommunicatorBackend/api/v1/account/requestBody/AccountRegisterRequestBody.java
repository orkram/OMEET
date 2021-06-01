package com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountRegisterRequestBody {
    private String username;
    private String eMail;
    private String firstName;
    private String lastName;
    private String imgUrl;
    private String password;

    @JsonCreator
    public AccountRegisterRequestBody(@JsonProperty("userName") String userName,
                                      @JsonProperty("eMail") String eMail,
                                      @JsonProperty("firstName") String firstName,
                                      @JsonProperty("lastName") String lastName,
                                      @JsonProperty("imgURL") String imgURL,
                                      @JsonProperty("password") String password) {
        this.username = userName;
        this.eMail = eMail;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imgUrl = imgURL;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setPassword(String password) {
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
