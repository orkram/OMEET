package com.orange.OrangeCommunicatorBackend.api.v1.users;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users")
@Api(tags = "Users")
@Slf4j
public class UsersApi {

    @GetMapping("/{id}")
    @ApiOperation("Find certain user")
    public String find(@PathVariable Long id) {
        return "/users GET {id} endpoint";
    }

    @PutMapping
    @ApiOperation("Update user")
    public String update(@RequestBody String s) {
        return "/users PUT endpoint";
    }

    @GetMapping
    @ApiOperation("Find all users")
    public String findAll() {
        return "/users GET endpoint";
    }

}
