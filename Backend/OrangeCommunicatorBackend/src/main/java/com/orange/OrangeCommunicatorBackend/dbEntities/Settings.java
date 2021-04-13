package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import javax.persistence.*;

@Entity(name="Settings")
@Data
public class Settings {

    @Id
    private String user_name;

    private String setting_1;

    private String setting_2;

    private String setting_3;

    @OneToOne
    @JoinColumn(name = "user_name")
    @MapsId
    private User user;

    Settings() {

    }

    public Settings(String user_name, String setting_1, String setting_2, String setting_3, User user) {
        this.user_name = user_name;
        this.setting_1 = setting_1;
        this.setting_2 = setting_2;
        this.setting_3 = setting_3;
        this.user = user;
    }

    public String getId_user() {
        return user_name;
    }

    public void setId_user(String id_user) {
        this.user_name = id_user;
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
