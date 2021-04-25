package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import javax.persistence.*;

@Entity(name = "ListOfFriends")
@IdClass(FriendshipId.class)
@Data
public class ListOfFriends {

    @Id
    @Column(name="user_name_owner")
    private String userNameOwner;

    @Id
    @Column(name="user_name_friend")
    private String userNameFrnd;


    @ManyToOne
    @JoinColumn(name = "user_name_owner")
    @MapsId
    private User userOwn;

    @ManyToOne
    @JoinColumn(name = "user_name_friend")
    @MapsId
    private User userFnd;

}
