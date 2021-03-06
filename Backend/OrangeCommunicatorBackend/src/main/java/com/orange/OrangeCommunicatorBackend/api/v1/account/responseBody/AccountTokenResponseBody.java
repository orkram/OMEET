//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountTokenResponseBody {

    private final String accessToken;
    private final long expiresIn;
    private final long refreshExpiresIn;
    private final String refreshToken;
    private final String tokenType;
    private final long notBeforePolicy;
    private final String sessionState;
    private final String scope;

    @JsonCreator
    public AccountTokenResponseBody(@JsonProperty("accessToken") String accessToken,
                                    @JsonProperty("expiresIn") long expiresIn,
                                    @JsonProperty("refreshExpiresIn") long refreshExpiresIn,
                                    @JsonProperty("refreshToken") String refreshToken,
                                    @JsonProperty("tokenType") String tokenType,
                                    @JsonProperty("notBeforePolicy") long notBeforePolicy,
                                    @JsonProperty("sessionState") String sessionState,
                                    @JsonProperty("scope") String scope) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.notBeforePolicy = notBeforePolicy;
        this.sessionState = sessionState;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public long getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getNotBeforePolicy() {
        return notBeforePolicy;
    }

    public String getSessionState() {
        return sessionState;
    }

    public String getScope() {
        return scope;
    }
}
