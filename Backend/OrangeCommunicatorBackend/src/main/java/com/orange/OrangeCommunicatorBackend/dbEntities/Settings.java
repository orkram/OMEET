package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import javax.persistence.*;

@Entity(name="Settings")
@Data
public class Settings {

    @Id
    private long id_user;

    private String setting_1;

    private String setting_2;

    private String setting_3;

    @OneToOne
    @JoinColumn(name = "id_user")
    @MapsId
    private User user;

    Settings() {

    }

    public Settings(long id_user, String setting_1, String setting_2, String setting_3, User user) {
        this.id_user = id_user;
        this.setting_1 = setting_1;
        this.setting_2 = setting_2;
        this.setting_3 = setting_3;
        this.user = user;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getSetting_1() {
        return setting_1;
    }

    public void setSetting_1(String setting_1) {
        this.setting_1 = setting_1;
    }

    public String getSetting_2() {
        return setting_2;
    }

    public void setSetting_2(String setting_2) {
        this.setting_2 = setting_2;
    }

    public String getSetting_3() {
        return setting_3;
    }

    public void setSetting_3(String setting_3) {
        this.setting_3 = setting_3;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
