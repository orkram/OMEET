//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.contacts;

import com.orange.OrangeCommunicatorBackend.api.v1.contacts.responseBody.SendInviteResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.contacts.support.AddErrorEnum;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/contacts")
@Api(tags = "User's list of friends")
@Slf4j
public class ContactsApi {

    private final ContactsService contactsService;

    public ContactsApi(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping(path="/add")
    @ApiOperation("Add friend")
    public String create(@ApiParam(value = "The username of one of users who wants to be friends", required = true)
                         @RequestParam("user-o") String userO,
                         @ApiParam(value = "The username of one of users who wants to be friends", required = true)
                         @RequestParam("user-f") String userF, Model model) {
        AddErrorEnum addErrorEnum = contactsService.add(userO, userF);

        String msg;
        HttpStatus status;

        if(addErrorEnum == AddErrorEnum.EXISTS){
            msg = "You are already friends";
            status = HttpStatus.FOUND;
        } else if(addErrorEnum == AddErrorEnum.NOT_FOUND){
            msg = "You cannot be friends :(";
            status = HttpStatus.NOT_FOUND;
        }
        else {
            msg = "You have become friends";
            status = HttpStatus.OK;
        }

        model.addAttribute("msg", msg);
        //model.;


        return "msg";

    }

    @GetMapping(path="/friends/{username}")
    @ApiOperation("Find friends of users")
    public @ResponseBody ResponseEntity<List<UserResponseBody>> find(@ApiParam(value = "The username of user whose friends should be found.", required = true)
                                                       @PathVariable String username,
                                                       @ApiParam(value = "The searching words, by which friends will be found.")
                                                       @RequestParam(name="query", defaultValue="") List<String> query,
                                                       @ApiParam(value = "The sort type by first name.")
                                                       @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                                       @ApiParam(value = "The sort type by last name.")
                                                       @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                                       @ApiParam(value = "The sort type by username.")
                                                       @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc,
                                                       @ApiParam(value = "The sort type by email.")
                                                       @RequestParam(name="emailSortAscending", defaultValue="true") boolean emailAsc,
                                                       @ApiParam(value = "Optionally get avatar.")
                                                       @RequestParam(name="getAvatar", defaultValue="true") boolean isGettingAvatar) {
        List<UserResponseBody> responseBodyList = contactsService.findAll(username, query, fNameAsc,
                lNameAsc, uNameAsc, emailAsc, isGettingAvatar);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodyList);
    }

    @GetMapping(path="/friends/{username}/page")
    @ApiOperation("Find friends of users paginated")
    public @ResponseBody ResponseEntity<FoundUsersPageResponseBody> findPaginated(@ApiParam(value = "The username of user whose friends should be found.", required = true)
                                                                    @PathVariable String username,
                                                                    @ApiParam(value = "The number of page to return.", required = true) @RequestParam("page") int page,
                                                                    @ApiParam(value = "The amount of users per page to return.", required = true) @RequestParam("size")  int size,
                                                                    @ApiParam(value = "The searching words, by which friends will be found.")
                                                                    @RequestParam(name="query", defaultValue="") List<String> query,
                                                                    @ApiParam(value = "The sort type by first name.")
                                                                    @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                                                    @ApiParam(value = "The sort type by last name.")
                                                                    @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                                                    @ApiParam(value = "The sort type by username.")
                                                                    @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc,
                                                                    @ApiParam(value = "The sort type by email.")
                                                                    @RequestParam(name="emailSortAscending", defaultValue="true") boolean emailAsc,
                                                                    @ApiParam(value = "Optionally get avatar.")
                                                                    @RequestParam(name="getAvatar", defaultValue="true") boolean isGettingAvatar) {
        FoundUsersPageResponseBody response = contactsService.findPaginated(page, size, username, query, fNameAsc,
                lNameAsc, uNameAsc, emailAsc, isGettingAvatar);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping(path="/friends/{username}")
    @ApiOperation("Delete friend from friend's list")
    public @ResponseBody ResponseEntity<Void> delete(@ApiParam(value = "The username of one of users to delete relationship between.", required = true) @PathVariable String username,
                                                     @ApiParam(value = "The username of one of users to delete relationship between.", required = true) @RequestParam("friend") String friend) {
        contactsService.delete(username, friend);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping(path="/send-invite")
    @ApiOperation("Send invite")
    @ApiResponses(value = {
            @ApiResponse(code = 302, message = "Friendship already exists"),
            @ApiResponse(code = 409, message = "Couldn't send an email")
    })
    public @ResponseBody ResponseEntity<Void> sendInvite
            (@ApiParam(value = "The username of user which is sending an email.", required = true) @RequestParam("from") String from,
             @ApiParam(value = "The username of user which is receiving an email.", required = true) @RequestParam("to") String to) {
        boolean sendInviteOk = sendInviteOk = contactsService.sendInvite(from, to);

        if(sendInviteOk){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).build();
        }

    }

    @GetMapping(path="/check-friendship")
    @ApiOperation("Check if two users are friends")
    public @ResponseBody ResponseEntity<Void> checkFriendship
            (@ApiParam(value = "", required = true) @RequestParam("from") String user1,
             @ApiParam(value = "The username of one of users to check relationship between.", required = true) @RequestParam("to") String user2) {
        contactsService.checkFriendship(user1, user2);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
