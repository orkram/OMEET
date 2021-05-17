package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import javax.persistence.*;

@Entity(name="Settings")
@Data
public class Settings {

    @Id
    @Column(name = "user_name")
    private String userName;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "is_mic_enabled_on_entry")
    private boolean isDefaultMicOn;

    @Column(name = "is_camera_enabled_on_entry")
    private boolean isDefaultCamOn;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_name")
    @MapsId
    private User user;

    public Settings() {

    }

    public Settings(boolean isPrivate, boolean isDefaultMicOn, boolean isDefaultCamOn, User user) {
        this.userName = user.getUserName();
        this.isPrivate = isPrivate;
        this.isDefaultMicOn = isDefaultMicOn;
        this.isDefaultCamOn = isDefaultCamOn;
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        this.userName = user.getUserName();
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isDefaultMicOn() {
        return isDefaultMicOn;
    }

    public void setDefaultMicOn(boolean defaultMicOn) {
        isDefaultMicOn = defaultMicOn;
    }

    public boolean isDefaultCamOn() {
        return isDefaultCamOn;
    }

    public void setDefaultCamOn(boolean defaultCamOn) {
        isDefaultCamOn = defaultCamOn;
    }
}
