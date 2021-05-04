package com.orange.OrangeCommunicatorBackend.dbRepositories;

import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.MeetingUserList;
import com.orange.OrangeCommunicatorBackend.dbEntities.ParticipantId;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingUserListRepository extends JpaRepository<MeetingUserList, ParticipantId> {
    List<MeetingUserList> findByUser(User user);
    List<MeetingUserList> findByMeeting(Meeting meeting);
}
