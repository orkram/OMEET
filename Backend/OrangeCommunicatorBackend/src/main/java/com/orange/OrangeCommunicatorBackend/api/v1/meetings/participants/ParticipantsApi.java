package com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/meetings/participants")
@Api(tags = "List of meeting's participants")
@Slf4j
public class ParticipantsApi {

    private final ParticipantsService participantsService;

    public ParticipantsApi(ParticipantsService participantsService) {
        this.participantsService = participantsService;
    }

    @PostMapping
    @ApiOperation("Add participant to meeting")
    public ResponseEntity<Void> create(
            @ApiParam(value = "The id of the meeting to which the user is to be added.", required = true) @RequestParam(name="meetingId") long id,
            @ApiParam(value = "The username of user to be added to the meeting.", required = true) @RequestParam(name="username") String username) {
        participantsService.create(id, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping()
    @ApiOperation("Remove participant from meeting")
    public ResponseEntity<Void> delete(
            @ApiParam(value = "The id of the meeting from which the user is to be removed.", required = true) @RequestParam(name="meetingId") long id,
            @ApiParam(value = "The username of user to be removed from the meeting.", required = true) @RequestParam(name="username") String username) {
        participantsService.delete(id, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(path="/meeting/{id}", produces = "application/json")
    @ApiOperation("Get meeting's participants")
    public ResponseEntity<List<UserResponseBody>> findParticipants(
                                                  @ApiParam(value = "The id of meeting for which participants should be returned.", required = true)
                                                  @PathVariable Long id,
                                                  @ApiParam(value = "The searching words, by which users will be found.")
                                                  @RequestParam(name="query", defaultValue="") List<String> query,
                                                  @ApiParam(value = "The sort type by first name.")
                                                  @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                                  @ApiParam(value = "The sort type by last name.")
                                                  @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                                  @ApiParam(value = "The sort type by username.")
                                                  @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc,
                                                  @ApiParam(value = "The sort type by email.")
                                                  @RequestParam(name="emailSortAscending", defaultValue="true") boolean emailAsc) {
        List<UserResponseBody> responseBodies = participantsService.findParticipants(id, query,
                fNameAsc,  lNameAsc, uNameAsc, emailAsc);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodies);
    }

    @GetMapping(path="/meeting/{id}/page", produces = "application/json")
    @ApiOperation("Get page of meeting's participants ")
    public ResponseEntity<FoundUsersPageResponseBody>
            findParticipantsPaginated(@ApiParam(value = "The id of meeting for which participants should be returned.", required = true)
                                      @PathVariable Long id,
                                      @ApiParam(value = "The number of page to return.", required = true)
                                      @RequestParam("page") int page,
                                      @ApiParam(value = "The amount of users per page to return.", required = true)
                                      @RequestParam("size")  int size,
                                      @ApiParam(value = "The searching words, by which users will be found.")
                                      @RequestParam(name="query", defaultValue="") List<String> query,
                                      @ApiParam(value = "The sort type by first name.")
                                      @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                      @ApiParam(value = "The sort type by last name.")
                                      @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                      @ApiParam(value = "The sort type by username.")
                                      @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc,
                                      @ApiParam(value = "The sort type by email.")
                                      @RequestParam(name="emailSortAscending", defaultValue="true") boolean emailAsc) {
        FoundUsersPageResponseBody responseBodies =
                participantsService.findParticipantsPaginated(id, query, page, size, fNameAsc, lNameAsc, uNameAsc, emailAsc);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodies);
    }


    @GetMapping(path="/user/{username}", produces = "application/json")
    @ApiOperation("Get user's meetings ")
    public ResponseEntity<List<MeetingResponseBody>>
            findMeetings(@ApiParam(value = "The username of user for which meetings should be returned.", required = true)
                         @PathVariable String username,
                         @ApiParam(value = "The searching words, by which user's meetings will be found.")
                         @RequestParam(name="query", defaultValue="") List<String> query,
                         @ApiParam(value = "The sort type by meeting's name.")
                         @RequestParam(name="meetingNameSortAscending", defaultValue="true") boolean mNameAsc,
                         @ApiParam(value = "The sort type by meeting's id.")
                         @RequestParam(name="idSortAscending", defaultValue="true") boolean idAsc,
                         @ApiParam(value = "The sort type by meeting's date.")
                         @RequestParam(name="meetingDateSortAscending", defaultValue="true") boolean dateAsc) {
        List<MeetingResponseBody> responseBodies = participantsService.findMeetings(username, query,
                mNameAsc, idAsc, dateAsc);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodies);
    }

    @GetMapping(path="/user/{username}/page", produces = "application/json")
    @ApiOperation("Get user's meetings ")
    public ResponseEntity<MeetingsPageResponseBody>
            findMeetingsPaginated(@ApiParam(value = "The username of user for which meetings should be returned.", required = true)
                                  @PathVariable String username,
                                  @ApiParam(value = "The searching words, by which user's meetings will be found.")
                                  @RequestParam(name="query", defaultValue="") List<String> query,
                                  @ApiParam(value = "The number of page to return.", required = true)
                                  @RequestParam("page") int page,
                                  @ApiParam(value = "The amount of meetings per page to return.", required = true)
                                  @RequestParam("size")  int size,
                                  @ApiParam(value = "The sort type by meeting's name.")
                                  @RequestParam(name="meetingNameSortAscending", defaultValue="true") boolean mNameAsc,
                                  @ApiParam(value = "The sort type by meeting's id.")
                                  @RequestParam(name="idSortAscending", defaultValue="true") boolean idAsc,
                                  @ApiParam(value = "The sort type by meeting's date.")
                                  @RequestParam(name="meetingDateSortAscending", defaultValue="true") boolean dateAsc) {
        MeetingsPageResponseBody responseBodies =
                participantsService.fingMeetingsPaginated(username, page, size, mNameAsc, query, idAsc, dateAsc);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodies);
    }

}
