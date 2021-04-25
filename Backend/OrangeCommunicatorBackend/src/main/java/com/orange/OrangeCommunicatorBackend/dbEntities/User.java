package com.orange.OrangeCommunicatorBackend.dbEntities;


import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Users")
@Data
public class User {

    @Id
    @Column(name="user_name")
    private String userName;

    @Column(name="e_mail", nullable=false, length = 32)
    private String eMail;

    @Column(name="first_name", nullable=false, length = 32)
    private String firstName;

    @Column(name="last_name", nullable=false, length = 32)
    private String lastName;

    @Column(name="img_url", nullable=true)
    private String imgUrl;

    @Column(name="keycloak_id", nullable = false)
    private String keycloakId;



    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Settings settings;

    @OneToMany(mappedBy = "user")
    private Set<Meeting> meetings;

    @OneToMany(mappedBy = "user")
    private Set<MeetingUserList> meetingUserList;

    @OneToMany(mappedBy = "userOwn")
    private Set<ListOfFriends> listOfFriends1;

    @OneToMany(mappedBy = "userFnd")
    private Set<ListOfFriends> listOfFriends2;

    public User(){

    }

    public User(String user_name, String e_mail, String first_name, String last_name, String img_url, String keycloak_id) {
        this.userName = user_name;
        this.eMail = e_mail;
        this.firstName = first_name;
        this.lastName = last_name;
        this.imgUrl = img_url;
        this.keycloakId = keycloak_id;
    }

    public User(String user_name, String e_mail, String first_name, String last_name, String img_url) {
        this.userName = user_name;
        this.eMail = e_mail;
        this.firstName = first_name;
        this.lastName = last_name;
        this.imgUrl = img_url;
        this.keycloakId = null;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getKeycloakId() {
        return keycloakId;
    }

    public void setKeycloakId(String keycloakId) {
        this.keycloakId = keycloakId;
    }
}
