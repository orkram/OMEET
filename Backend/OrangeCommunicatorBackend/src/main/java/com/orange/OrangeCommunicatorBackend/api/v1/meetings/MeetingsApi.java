package com.orange.OrangeCommunicatorBackend.api.v1.meetings;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.NewMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.UpdateMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/meetings")
@Api(tags = "List of meetings")
@Slf4j
public class MeetingsApi {

    private final MeetingsService meetingsService;

    public MeetingsApi(MeetingsService meetingsService) {
        this.meetingsService = meetingsService;
    }


    @PostMapping(produces = "application/json")
    @ApiOperation("Create new meeting")
    public ResponseEntity<MeetingResponseBody> create(@RequestBody NewMeetingRequestBody newMeetingRequestBody) {
        MeetingResponseBody meetingResponseBody = meetingsService.create(newMeetingRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(meetingResponseBody);
    }

    @GetMapping(path="/{id}", produces = "application/json")
    @ApiOperation("Get informations about meeting")
    public ResponseEntity<MeetingResponseBody> find(
            @ApiParam(value = "The id of meeting for which information should be returned.", required = true) @PathVariable Long id) {
        MeetingResponseBody meetingResponseBody = meetingsService.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(meetingResponseBody);
    }

    @DeleteMapping(path="/{id}", produces = "application/json")
    @ApiOperation("Cancel meeting")
    public ResponseEntity<Void> delete(
            @ApiParam(value = "The id of meeting which should be removed.", required = true) @PathVariable Long id) {
        meetingsService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(path="/{id}", produces = "application/json")
    @ApiOperation("Update meeting")
    public ResponseEntity<MeetingResponseBody> update(
            @ApiParam(value = "The id of meeting for which information should be updated.", required = true) @PathVariable Long id,
            @RequestBody UpdateMeetingRequestBody updateMeetingRequestBody) {
        MeetingResponseBody meetingResponseBody = meetingsService.update(id, updateMeetingRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(meetingResponseBody);
    }

    @GetMapping(path="/owner/{username}", produces = "application/json")
    @ApiOperation("Get all meetings of owner")
    public ResponseEntity<List<MeetingResponseBody>>
            getOwnersMeetings(@ApiParam(value = "The username of user for which meetings should be returned.", required = true) @PathVariable String username,
                              @ApiParam(value = "The searching words, by which user's meetings will be found.")
                              @RequestParam(name="query", defaultValue="") List<String> query,
                              @ApiParam(value = "The sort type by meeting's name.")
                              @RequestParam(name="meetingNameSortAscending", defaultValue="true") boolean mNameAsc,
                              @ApiParam(value = "The sort type by meeting's id.")
                              @RequestParam(name="idSortAscending", defaultValue="true") boolean idAsc,
                              @ApiParam(value = "The sort type by meeting's date.")
                              @RequestParam(name="meetingDateSortAscending", defaultValue="true") boolean dateAsc) {
        List<MeetingResponseBody> meetingResponseBody = meetingsService.getOwnersMeeting(username, query, mNameAsc,
                idAsc, dateAsc);
        return ResponseEntity.status(HttpStatus.OK).body(meetingResponseBody);
    }

    @GetMapping(path="/owner/{username}/page", produces = "application/json")
    @ApiOperation("Get all meetings of owner paginated")
    public ResponseEntity<MeetingsPageResponseBody>
            getOwnersMeetingsPaginated(@ApiParam(value = "The username of user for which meetings should be returned.", required = true) @PathVariable String username,
                                       @ApiParam(value = "The number of page to return.", required = true) @RequestParam("page") int page,
                                       @ApiParam(value = "The amount of meetings per page to return.", required = true) @RequestParam("size")  int size,
                                       @ApiParam(value = "The searching words, by which user's meetings will be found.")
                                       @RequestParam(name="query", defaultValue="") List<String> query,
                                       @ApiParam(value = "The sort type by meeting's name.")
                                       @RequestParam(name="meetingNameSortAscending", defaultValue="true") boolean mNameAsc,
                                       @ApiParam(value = "The sort type by meeting's id.")
                                       @RequestParam(name="idSortAscending", defaultValue="true") boolean idAsc,
                                       @ApiParam(value = "The sort type by meeting's date.")
                                       @RequestParam(name="meetingDateSortAscending", defaultValue="true") boolean dateAsc) {
        MeetingsPageResponseBody meetingResponseBody =
                meetingsService.getOwnersMeetingPaginated(username, query, page, size, mNameAsc, idAsc, dateAsc);
        return ResponseEntity.status(HttpStatus.OK).body(meetingResponseBody);
    }


}
