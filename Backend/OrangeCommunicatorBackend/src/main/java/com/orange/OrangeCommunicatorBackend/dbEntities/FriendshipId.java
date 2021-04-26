package com.orange.OrangeCommunicatorBackend.dbEntities;


import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class FriendshipId implements Serializable {

    private String userNameOwner;
    private String userNameFrnd;

    public FriendshipId(String userNameOwner, String userNameFrnd) {
        this.userNameOwner = userNameOwner;
        this.userNameFrnd = userNameFrnd;
    }

    public FriendshipId(){

    }

    public String getUserNameOwner() {
        return userNameOwner;
    }

    public void setUserNameOwner(String userNameOwner) {
        this.userNameOwner = userNameOwner;
    }

    public String getUserNameFrnd() {
        return userNameFrnd;
    }

    public void setUserNameFrnd(String userNameFrnd) {
        this.userNameFrnd = userNameFrnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendshipId that = (FriendshipId) o;
        return userNameOwner.equals(that.userNameOwner) && userNameFrnd.equals(that.userNameFrnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userNameOwner, userNameFrnd);
    }
}
