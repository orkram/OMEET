package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "ListOfFriends")
@IdClass(FriendshipId.class)
@Data
public class ListOfFriends {

    @Id
    private String user_name_owner;

    @Id
    private String user_name_frnd;


    @ManyToOne
    @JoinColumn(name = "user_name_owner")
    @MapsId
    private User user_o;

    @ManyToOne
    @JoinColumn(name = "user_name_frnd")
    @MapsId
    private User user_f;

}
