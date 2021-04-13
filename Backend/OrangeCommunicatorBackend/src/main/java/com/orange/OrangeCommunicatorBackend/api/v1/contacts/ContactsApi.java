package com.orange.OrangeCommunicatorBackend.api.v1.contacts;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/contacts")
@Api(tags = "User's list of friends")
@Slf4j
public class ContactsApi {

    @PostMapping
    @ApiOperation("Add friend")
    public String create(@RequestBody String s) {
        return "/contacts POST endpoint";
    }

    @GetMapping("/{id}")
    @ApiOperation("Find friends of users")
    public String find(@PathVariable Long id) {
        return "/contacts GET endpoint";
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete friend from friend's list")
    public String delete(@PathVariable Long id) {
        return "/contacts DELETE endpoint";
    }

}
