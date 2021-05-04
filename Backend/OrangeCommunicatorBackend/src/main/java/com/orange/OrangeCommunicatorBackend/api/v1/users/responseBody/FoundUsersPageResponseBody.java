package com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class FoundUsersPageResponseBody {

    private final long allFoundUsers;
    private final long allFoundPages;
    private final List<UserResponseBody> found_users;

    @JsonCreator
    public FoundUsersPageResponseBody(long allFoundUsers, long allFoundPages, List<UserResponseBody> found_users) {
        this.allFoundUsers = allFoundUsers;
        this.allFoundPages = allFoundPages;
        this.found_users = found_users;
    }

    public long getAllFoundUsers() {
        return allFoundUsers;
    }

    public long getAllFoundPages() {
        return allFoundPages;
    }

    public List<UserResponseBody> getFoundUsers() {
        return found_users;
    }

}
