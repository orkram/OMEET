//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateAvatarResponseBody {

    private final String userName;
    private final String imgUpdateUrl;

    @JsonCreator
    public UpdateAvatarResponseBody(@JsonProperty("userName") String userName,
                                    @JsonProperty("imgUpdateUrl") String imgUpdateUrl) {
        this.userName = userName;
        this.imgUpdateUrl = imgUpdateUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getImgUpdateUrl() {
        return imgUpdateUrl;
    }
}
