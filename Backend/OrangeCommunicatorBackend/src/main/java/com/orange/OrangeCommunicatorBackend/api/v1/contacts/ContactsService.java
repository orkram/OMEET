package com.orange.OrangeCommunicatorBackend.api.v1.contacts;

import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.AddErrorEnum;
import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.ContactExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.ContactMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.exceptions.FriendshipNotFoundException;
import com.orange.OrangeCommunicatorBackend.api.v1.users.UserService;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserSupport;
import com.orange.OrangeCommunicatorBackend.dbEntities.FriendshipId;
import com.orange.OrangeCommunicatorBackend.dbEntities.ListOfFriends;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.ListOfFriendsRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import com.orange.OrangeCommunicatorBackend.generalServicies.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ContactsService {

    private final ContactMapper contactMapper;
    private final ListOfFriendsRepository listOfFriendsRepository;
    private final MailService mailService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserSupport userSupport;

    private static final String subject = "Invite";

    @Value("${server.url}") String url;
    @Value("${server.port}") String port;

    private static final String userColName = "userName";
    private static final String nameColName = "firstName";
    private static final String surnameColName = "lastName";

    public ContactsService(ContactMapper contactMapper, ListOfFriendsRepository listOfFriendsRepository, MailService mailService, UserRepository userRepository, UserMapper userMapper, UserSupport userSupport) {
        this.contactMapper = contactMapper;
        this.listOfFriendsRepository = listOfFriendsRepository;
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userSupport = userSupport;
    }

    public boolean sendInvite(String from, String to) throws IllegalArgumentException {

        FriendshipId friendshipId = new FriendshipId(from, to);
        ListOfFriends l = listOfFriendsRepository.findById(friendshipId).orElse(null);
        if (l != null){
            return false;
        }
        friendshipId = new FriendshipId(to, from);
        l = listOfFriendsRepository.findById(friendshipId).orElse(null);
        if (l != null){
            return false;
        }

        User user = userRepository.findById(to).orElseThrow(UserExceptionSupplier.userNotFoundException(to));

        String email = user.getEMail();

        User sender = userRepository.findById(from).orElseThrow(UserExceptionSupplier.userNotFoundException(from));
        String senderName = sender.getFirstName() + " " + sender.getLastName();



        String path = getUrl(from, to);
        String text = "Invite";

        String emailText = "Hey,<br>An invitation was sent to you by " + senderName + " to join his group of friends!!<br>";
        emailText += "To accept this invitation click the link below:<br>";
        emailText += "<form method=\"post\" action=\"" + path +"\"> <button type=\"submit\">" + text + "</button></form>";

        try {
            mailService.sendMail(email, subject, emailText, true);
        } catch (MessagingException e) {
            throw ContactExceptionSupplier.emailNotSentException(user).get();
        }

        return true;
    }

    public void delete(String username, String friend) {

        FriendshipId friendshipId = new FriendshipId(username, friend);
        ListOfFriends l = listOfFriendsRepository.findById(friendshipId).orElse(null);
        if (l == null){
            friendshipId = new FriendshipId(friend, username);
            l = listOfFriendsRepository.findById(friendshipId)
                    .orElseThrow(ContactExceptionSupplier.friendshipNotFoundException(friendshipId));
        }

        listOfFriendsRepository.deleteById(friendshipId);
    }


    private String getUrl(String uo, String uf){
        return url + ":" + port + "/api/v1/contacts/add?user-o=" + uo + "&user-f=" + uf;
    }


    public AddErrorEnum add(String userO, String userF) {
        ListOfFriends l = listOfFriendsRepository.findById(new FriendshipId(userO, userF)).orElse(null);
        if (l != null){
            return AddErrorEnum.EXISTS;
        }
        l = listOfFriendsRepository.findById(new FriendshipId(userF, userO)).orElse(null);
        if (l != null){
            return AddErrorEnum.EXISTS;
        }

        User userT1 = userRepository.findById(userO).orElse(null);
        if(userT1 == null)
            return  AddErrorEnum.NOT_FOUND;

        User userT2 = userRepository.findById(userF).orElse(null);
        if(userT2 == null)
            return  AddErrorEnum.NOT_FOUND;

        ListOfFriends listOfFriends = new ListOfFriends();
        listOfFriends.setUserOwn(userT1);
        listOfFriends.setUserFnd(userT2);

        listOfFriendsRepository.save(listOfFriends);
        return AddErrorEnum.OK;
    }

    public List<UserResponseBody> findAll(String username, List<String> query) {
        return findAllOrder(username, query, true, true, true);
    }

    public FoundUsersPageResponseBody findPaginated(int page, int size, String username, List<String> query,
                                                    boolean fNameAsc, boolean lNameAsc, boolean uNameAsc) {


        List<UserResponseBody> responseBodies = findAllOrder(username, query, true, true, true);
        if(page <= 0){
            page = 1;
        }

        if(size <= 0){
            size = 1;
        }

        List<UserResponseBody> responseBodiesPage = responseBodies.subList((page - 1) * size, page * size);

        return userMapper.toUserFoundPaged(responseBodiesPage, responseBodies.size(), responseBodies.size()/size + 1);
    }

    private List<UserResponseBody> findAllOrder(String username, List<String> query,
                                                boolean fNameAsc, boolean lNameAsc, boolean uNameAsc){
        User u = userRepository.findById(username)
                .orElseThrow(UserExceptionSupplier.userNotFoundException(username));

        Sort sort = userSupport.getSort(fNameAsc, lNameAsc, uNameAsc);

        if(query.size() == 0){
            query.add("");
        }

        Specification<User> spec = userSupport.nameContains(query);
        List<User> users = userRepository.findAll(spec, sort);

        List<ListOfFriends> l1 = listOfFriendsRepository.findByUserOwn(u);
        List<String> ls1 = l1.stream().map(contactMapper::getUserFrndName).collect(Collectors.toList());
        List<ListOfFriends> l2 = listOfFriendsRepository.findByUserFnd(u);
        List<String> ls2 = l2.stream().map(contactMapper::getUserOwnerName).collect(Collectors.toList());

        List<String> usernamesList = Stream.concat(ls1.stream(), ls2.stream())
                .collect(Collectors.toList());

        List<User> finalUsersList = new ArrayList<>();

        for (User user : users){
            if(usernamesList.contains(user.getUserName()))
                finalUsersList.add(user);
        }
        List<UserResponseBody>  usersResponse = finalUsersList.stream().map(userMapper::toUserResponseBody).collect(Collectors.toList());
        return usersResponse;
    }
}


