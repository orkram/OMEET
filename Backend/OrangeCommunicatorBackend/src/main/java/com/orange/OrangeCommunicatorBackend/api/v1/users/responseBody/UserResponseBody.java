package com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponseBody {
    private final String username;
    private final String eMail;
    private final String firstName;
    private final String lastName;
    private final String imgUrl;


    @JsonCreator
    public UserResponseBody(@JsonProperty("userName") String userName,
                            @JsonProperty("eMail") String eMail,
                            @JsonProperty("firstName") String firstName,
                            @JsonProperty("lastName") String lastName,
                            @JsonProperty("imgUrl") String imgUrl) {
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
