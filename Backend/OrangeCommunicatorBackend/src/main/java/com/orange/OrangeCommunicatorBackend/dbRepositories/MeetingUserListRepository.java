//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.dbRepositories;

import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.MeetingUserList;
import com.orange.OrangeCommunicatorBackend.dbEntities.ParticipantId;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingUserListRepository extends JpaRepository<MeetingUserList, ParticipantId> {
    List<MeetingUserList> findByUser(User user);
    List<MeetingUserList> findByMeeting(Meeting meeting);
}
