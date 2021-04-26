package com.orange.OrangeCommunicatorBackend.dbRepositories;

import com.orange.OrangeCommunicatorBackend.dbEntities.FriendshipId;
import com.orange.OrangeCommunicatorBackend.dbEntities.ListOfFriends;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListOfFriendsRepository extends JpaRepository<ListOfFriends, FriendshipId> {
    public List<ListOfFriends> findByUserOwn(User userOwn);
    public List<ListOfFriends> findByUserFnd(User userFnd);
}
