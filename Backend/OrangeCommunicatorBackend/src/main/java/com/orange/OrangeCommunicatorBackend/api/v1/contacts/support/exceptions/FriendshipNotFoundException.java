//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.exceptions;

import com.orange.OrangeCommunicatorBackend.dbEntities.FriendshipId;

public class FriendshipNotFoundException extends RuntimeException{
    public FriendshipNotFoundException(FriendshipId id){
        super(String.format("User %s and user %s are not friends", id.getUserNameOwner(), id.getUserNameFrnd()));
    }
}
