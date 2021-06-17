//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.contacts.support;

import com.orange.OrangeCommunicatorBackend.dbEntities.ListOfFriends;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public String getUserOwnerName(ListOfFriends listOfFriends){
        return listOfFriends.getUserNameOwner();
    }
    public String getUserFrndName(ListOfFriends listOfFriends){
        return listOfFriends.getUserNameFrnd();
    }


}
