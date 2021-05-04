package com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    public ResponseEntity<Void> create(@RequestParam(name="meetingId") long id, @RequestParam(name="username") String username) {
        participantsService.create(id, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    @ApiOperation("Remove participant from meeting")
    public ResponseEntity<Void> delete(@RequestParam(name="meetingId") long id, @RequestParam(name="username") String username) {
        participantsService.delete(id, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/meeting/{id}")
    @ApiOperation("Get meeting's participants")
    public ResponseEntity<List<UserResponseBody>> findParticipants(@PathVariable Long id,
                                                   @RequestParam(name="query", defaultValue="") List<String> query,
                                                   @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                                   @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                                   @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc) {
        List<UserResponseBody> responseBodies = participantsService.findParticipants(id, query,
                fNameAsc,  lNameAsc, uNameAsc);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodies);
    }

    @GetMapping("/meeting/{id}/page")
    @ApiOperation("Get page of meeting's participants ")
    public ResponseEntity<FoundUsersPageResponseBody>
            findParticipantsPaginated(@PathVariable Long id, @RequestParam(name="query", defaultValue="") List<String> query,
                                      @RequestParam("page") int page, @RequestParam("size")  int size,
                                      @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                      @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                      @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc) {
        FoundUsersPageResponseBody responseBodies =
                participantsService.findParticipantsPaginated(id, query, page, size, fNameAsc, lNameAsc, uNameAsc);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodies);
    }


    @GetMapping("/users/{username}")
    @ApiOperation("Get user's meetings ")
    public ResponseEntity<List<MeetingResponseBody>>
            findMeetings(@PathVariable String username, @RequestParam(name="query", defaultValue="") List<String> query,
                         @RequestParam(name="meetingNameSortAscending", defaultValue="true") boolean mNameAsc) {
        List<MeetingResponseBody> responseBodies = participantsService.findMeetings(username, query, mNameAsc);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodies);
    }

    @GetMapping("/users/{username}/page")
    @ApiOperation("Get user's meetings ")
    public ResponseEntity<MeetingsPageResponseBody>
            findMeetingsPaginated(@PathVariable String username, @RequestParam(name="query", defaultValue="") List<String> query,
                                  @RequestParam("page") int page, @RequestParam("size")  int size,
                                  @RequestParam(name="meetingNameSortAscending", defaultValue="true") boolean mNameAsc) {
        MeetingsPageResponseBody responseBodies =
                participantsService.fingMeetingsPaginated(username, page, size, mNameAsc, query);
        return ResponseEntity.status(HttpStatus.OK).body(responseBodies);
    }

}
