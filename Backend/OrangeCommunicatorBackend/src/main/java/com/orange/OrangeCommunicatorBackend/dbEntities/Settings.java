package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import javax.persistence.*;

@Entity(name="Settings")
@Data
public class Settings {

    @Id
    @Column(name = "user_name")
    private String userName;

    private String setting1;

    private String setting2;

    private String setting3;

    @OneToOne
    @JoinColumn(name = "user_name")
    @MapsId
    private User user;

    Settings() {

    }

    public Settings(String userName, String setting1, String setting2, String setting3, User user) {
        this.userName = userName;
        this.setting1 = setting1;
        this.setting2 = setting2;
        this.setting3 = setting3;
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String id_user) {
        this.userName = id_user;
    }

    public String getSetting1() {
        return setting1;
    }

    public void setSetting1(String setting1) {
        this.setting1 = setting1;
    }

    public String getSetting2() {
        return setting2;
    }

    public void setSetting2(String setting2) {
        this.setting2 = setting2;
    }

    public String getSetting3() {
        return setting3;
    }

    public void setSetting3(String setting3) {
        this.setting3 = setting3;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
