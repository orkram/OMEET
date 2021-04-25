package com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

public class FoundUsersPageResponseBody {

    private final long all_found_users;
    private final long all_found_pages;
    private final List<UserResponseBody> found_users;

    @JsonCreator
    public FoundUsersPageResponseBody(long all_found_users, long all_found_pages, List<UserResponseBody> found_users) {
        this.all_found_users = all_found_users;
        this.all_found_pages = all_found_pages;
        this.found_users = found_users;
    }

    public long getAll_found_users() {
        return all_found_users;
    }

    public long getAll_found_pages() {
        return all_found_pages;
    }

    public List<UserResponseBody> getFound_users() {
        return found_users;
    }

}
