package com.orange.OrangeCommunicatorBackend.dbEntities;


import lombok.Data;

import java.io.Serializable;

@Data
public class FriendshipId implements Serializable {

    private String userNameOwner;

    private String userNameFrnd;



}
