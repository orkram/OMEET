package com.orange.OrangeCommunicatorBackend.dbEntities;


import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "Users")
@Data
public class User {

    @Id
    @Column(name="user_name")
    private String user_name;

    @Column(name="e_mail", nullable=false, length = 32)
    private String e_mail;

    @Column(name="first_name", nullable=false, length = 32)
    private String first_name;

    @Column(name="last_name", nullable=false, length = 32)
    private String last_name;

    @Column(name="img_url", nullable=true)
    private String img_url;



    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Settings settings;

    @OneToMany(mappedBy = "user")
    private Set<Meeting> meetings;

    @OneToMany(mappedBy = "user")
    private Set<MeetingUserList> meetingUserList;

    @OneToMany(mappedBy = "user_o")
    private Set<ListOfFriends> listOfFriends1;

    @OneToMany(mappedBy = "user_f")
    private Set<ListOfFriends> listOfFriends2;

    public User(){

    }



    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
