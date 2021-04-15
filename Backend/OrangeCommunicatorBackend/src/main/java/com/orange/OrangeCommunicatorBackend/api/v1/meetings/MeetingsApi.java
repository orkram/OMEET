package com.orange.OrangeCommunicatorBackend.api.v1.meetings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/meetings")
@Api(tags = "List of meetings")
@Slf4j
public class MeetingsApi {

    @PostMapping
    @ApiOperation("Create new meeting")
    public String create(@RequestBody String s) {
        return "/meetings POST endpoint";
    }

    @GetMapping("/{id}")
    @ApiOperation("Get informations about meeting")
    public String find(@PathVariable Long id) {
        return "/meetings GET endpoint";
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Cancel meeting")
    public String delete(@PathVariable Long id) {
        return "/meetings DELETE endpoint";
    }

    @PutMapping("/{id}")
    @ApiOperation("Update meeting")
    public String update(@PathVariable Long id) {
        return "/meetings PUT endpoint";
    }


}
