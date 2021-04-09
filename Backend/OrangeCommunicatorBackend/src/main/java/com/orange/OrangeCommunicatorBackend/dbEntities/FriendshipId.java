package com.orange.OrangeCommunicatorBackend.dbEntities;


import lombok.Data;

import java.io.Serializable;

@Data
public class FriendshipId implements Serializable {

    private long id_user_owner;

    private long id_user_frnd;



}
