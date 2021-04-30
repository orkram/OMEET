package com.orange.OrangeCommunicatorBackend.dbRepositories;

import com.orange.OrangeCommunicatorBackend.dbEntities.ListOfFriends;
import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeetingRepository extends CrudRepository<Meeting, Long>, JpaSpecificationExecutor<Meeting> {
    public List<Meeting> findByUser(User owner);
}
