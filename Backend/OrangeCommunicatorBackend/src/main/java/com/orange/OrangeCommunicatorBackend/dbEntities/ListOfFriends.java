package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;
import org.keycloak.jose.jwk.JWK;

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

    public ListOfFriends(User userO, User userF){
        this.userOwn = userO;
        this.userFnd = userF;

        this.userNameOwner = userO.getUserName();
        this.userNameFrnd = userF.getUserName();
    }

    public ListOfFriends(){

    }

    public User getUserOwn() {
        return userOwn;
    }

    public void setUserOwn(User userOwn) {
        this.userOwn = userOwn;
        this.userNameOwner = userOwn.getUserName();
    }

    public User getUserFnd() {
        return userFnd;
    }

    public void setUserFnd(User userFnd) {
        this.userFnd = userFnd;
        this.userNameFrnd = userFnd.getUserName();
    }
}
