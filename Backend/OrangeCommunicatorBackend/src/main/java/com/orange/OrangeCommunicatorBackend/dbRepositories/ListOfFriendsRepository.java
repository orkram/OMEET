package com.orange.OrangeCommunicatorBackend.dbRepositories;

import com.orange.OrangeCommunicatorBackend.dbEntities.FriendshipId;
import com.orange.OrangeCommunicatorBackend.dbEntities.ListOfFriends;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListOfFriendsRepository extends JpaRepository<ListOfFriends, FriendshipId> {
}
