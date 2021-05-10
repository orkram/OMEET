package com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/meetings/participants")
@Api(tags = "List of meeting's participants")
@Slf4j
public class ParticipantsApi {

    @PostMapping
    @ApiOperation("Add participant to  meeting")
    public String create(@RequestBody String s) {
        return "/meetings/participants POST endpoint";
    }

    @GetMapping("/{id}")
    @ApiOperation("Get participants of meeting")
    public String find(@PathVariable Long id) {
        return "/meetings/participants GET endpoint";
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Remove participant from meeting")
    public String delete(@PathVariable Long id) {
        return "/meetings/participants DELETE endpoint";
    }

}
