package com.orange.OrangeCommunicatorBackend.dbEntities;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParticipantId implements Serializable {

    private long id_meeting;

    private long id_user;

}
