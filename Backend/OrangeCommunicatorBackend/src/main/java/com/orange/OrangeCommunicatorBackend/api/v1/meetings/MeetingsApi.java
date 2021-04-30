package com.orange.OrangeCommunicatorBackend.api.v1.meetings;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.NewMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.UpdateMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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


    @PostMapping
    @ApiOperation("Create new meeting")
    public ResponseEntity<MeetingResponseBody> create(@RequestBody NewMeetingRequestBody newMeetingRequestBody) {
        MeetingResponseBody meetingResponseBody = meetingsService.create(newMeetingRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(meetingResponseBody);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get informations about meeting")
    public ResponseEntity<MeetingResponseBody> find(@PathVariable Long id) {
        MeetingResponseBody meetingResponseBody = meetingsService.get(id);
        return ResponseEntity.status(HttpStatus.OK).body(meetingResponseBody);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Cancel meeting")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        meetingsService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @ApiOperation("Update meeting")
    public ResponseEntity<MeetingResponseBody> update(@PathVariable Long id,
                                                      @RequestBody UpdateMeetingRequestBody updateMeetingRequestBody) {
        MeetingResponseBody meetingResponseBody = meetingsService.update(id, updateMeetingRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(meetingResponseBody);
    }

    @GetMapping("/{username}")
    @ApiOperation("Get all meetings of owner")
    public ResponseEntity<List<MeetingResponseBody>> getOwnersMeetings(@PathVariable String username,
                                                                      @RequestParam(name="query", defaultValue="") List<String> query) {
        List<MeetingResponseBody> meetingResponseBody = meetingsService.getOwnersMeeting(username, query);
        return ResponseEntity.status(HttpStatus.OK).body(meetingResponseBody);
    }

    @GetMapping("/{username}/page")
    @ApiOperation("Get all meetings of owner paginated")
    public ResponseEntity<MeetingsPageResponseBody> getOwnersMeetingsPaginated(@PathVariable String username,
                                                                @RequestParam("page") int page, @RequestParam("size")  int size,
                                                                @RequestParam(name="query", defaultValue="") List<String> query,
                                                                @RequestParam(name="meetingNameSortAscending", defaultValue="true") boolean mNameAsc) {
        MeetingsPageResponseBody meetingResponseBody =
                meetingsService.getOwnersMeetingPaginated(username, query, page, size, mNameAsc);
        return ResponseEntity.status(HttpStatus.OK).body(meetingResponseBody);
    }


}
