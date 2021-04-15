package com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AccountTokenResponseBody {

    private final String access_token;
    private final long expires_in;
    private final long refresh_expires_in;
    private final String refresh_token;
    private final String token_type;
    private final long not_before_policy;
    private final String session_state;
    private final String scope;

    @JsonCreator
    public AccountTokenResponseBody(String access_token, long expires_in, long refresh_expires_in,
                                    String refresh_token, String token_type, long not_before_policy,
                                    String session_state, String scope) {
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_expires_in = refresh_expires_in;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.not_before_policy = not_before_policy;
        this.session_state = session_state;
        this.scope = scope;
    }

    public String getAccess_token() {
        return access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public long getRefresh_expires_in() {
        return refresh_expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public long getNot_before_policy() {
        return not_before_policy;
    }

    public String getSession_state() {
        return session_state;
    }

    public String getScope() {
        return scope;
    }
}
