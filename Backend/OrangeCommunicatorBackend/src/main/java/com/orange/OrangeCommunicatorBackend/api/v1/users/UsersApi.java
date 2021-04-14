package com.orange.OrangeCommunicatorBackend.api.v1.users;

import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users")
@Api(tags = "Users")
@Slf4j
public class UsersApi {

    private final UserService userService;

    public UsersApi(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    @ApiOperation("Get certain user's data")
    public ResponseEntity<UserResponseBody> find(@PathVariable String username) {
        UserResponseBody userResponseBody = userService.getUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseBody);
    }

    @PutMapping("/{username}")
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
