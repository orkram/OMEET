package com.orange.OrangeCommunicatorBackend.api.v1.users.settings;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users/settings")
@Api(tags = "User's settings")
@Slf4j
public class SettingsApi {

    @GetMapping("/{id}")
    @ApiOperation("Find certain user's settings")
    public String find(@PathVariable Long id) {
        return "/users/settings GET {id} endpoint";
    }

    @PutMapping
    @ApiOperation("Update user's settings")
    public String update(@RequestBody String s) {
        return "/users/settings PUT endpoint";
    }

}
