package com.orange.OrangeCommunicatorBackend.dbRepositories;

import com.orange.OrangeCommunicatorBackend.dbEntities.FriendshipId;
import com.orange.OrangeCommunicatorBackend.dbEntities.ListOfFriends;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListOfFriendsRepository extends JpaRepository<ListOfFriends, FriendshipId> {
    public List<ListOfFriends> findByUserOwn(User userOwn);
    public List<ListOfFriends> findByUserFnd(User userFnd);
}
