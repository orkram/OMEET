package com.orange.OrangeCommunicatorBackend.api.v1.contacts;

import com.orange.OrangeCommunicatorBackend.api.v1.contacts.responseBody.SendInviteResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.AddErrorEnum;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/contacts")
@Api(tags = "User's list of friends")
@Slf4j
public class ContactsApi {

    private final ContactsService contactsService;

    public ContactsApi(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @PostMapping("/add")
    @ApiOperation("Add friend")
    public String create(@RequestParam("user-o") String userO, @RequestParam("user-f") String userF) {
        AddErrorEnum addErrorEnum = contactsService.add(userO, userF);
        if(addErrorEnum == AddErrorEnum.EXISTS){
            return "You are already friends";
        } else if(addErrorEnum == AddErrorEnum.NOT_FOUND){
            return "You cannot be friends :(";
        }
        else {
            return "You have become friends";
        }
    }

    @GetMapping("/{username}")
    @ApiOperation("Find friends of users")
    public ResponseEntity<List<UserResponseBody>> find(@PathVariable String username, @RequestParam(name="query", defaultValue="") List<String> query,
                                                       @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                                       @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                                       @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc) {
        List<UserResponseBody> responseBodyList = contactsService.findAll(username, query, fNameAsc, lNameAsc, uNameAsc);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodyList);
    }

    @GetMapping("/{username}/page")
    @ApiOperation("Find friends of users paginated")
    public ResponseEntity<FoundUsersPageResponseBody> findPaginated(@PathVariable String username, @RequestParam("page") int page,
                                                                    @RequestParam("size")  int size,
                                                                    @RequestParam(name="query", defaultValue="") List<String> query,
                                                                    @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                                                    @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                                                    @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc) {
        FoundUsersPageResponseBody response = contactsService.findPaginated(page, size, username, query, fNameAsc, lNameAsc, uNameAsc);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{username}")
    @ApiOperation("Delete friend from friend's list")
    public ResponseEntity<Void> delete(@PathVariable String username, @RequestParam("friend") String friend) {
        contactsService.delete(username, friend);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/send-invite")
    @ApiOperation("Send invite")
    @ApiResponses(value = {
            @ApiResponse(code = 302, message = "Friendship already exists"),
            @ApiResponse(code = 409, message = "Couldn't send an email")
    })
    public ResponseEntity<SendInviteResponseBody> sendInvite(@RequestParam("from") String from, @RequestParam("to") String to) {
        boolean sendInviteOk = sendInviteOk = contactsService.sendInvite(from, to);

        if(sendInviteOk){
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

    }

    @GetMapping("/check-friendship")
    @ApiOperation("Check if two users are friends")
    public ResponseEntity<Void> checkFriendship(@RequestParam("from") String user1, @RequestParam("to") String user2) {
        contactsService.checkFriendship(user1, user2);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
