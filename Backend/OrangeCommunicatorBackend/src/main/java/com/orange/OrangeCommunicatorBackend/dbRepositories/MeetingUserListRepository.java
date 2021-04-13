package com.orange.OrangeCommunicatorBackend.dbRepositories;

import com.orange.OrangeCommunicatorBackend.dbEntities.MeetingUserList;
import com.orange.OrangeCommunicatorBackend.dbEntities.ParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingUserListRepository extends JpaRepository<MeetingUserList, ParticipantId> {
}
