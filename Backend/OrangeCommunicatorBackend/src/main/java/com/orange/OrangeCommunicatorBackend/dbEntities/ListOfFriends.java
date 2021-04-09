package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "ListOfFriends")
@IdClass(FriendshipId.class)
@Data
public class ListOfFriends {

    @Id
    private long id_user_owner;

    @Id
    private long id_user_frnd;


    @ManyToOne
    @JoinColumn(name = "id_user_owner")
    @MapsId
    private User user_o;

    @ManyToOne
    @JoinColumn(name = "id_user_frnd")
    @MapsId
    private User user_f;

}
