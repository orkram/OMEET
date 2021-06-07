package com.orange.OrangeCommunicatorBackend.api.v1.users;

import com.orange.OrangeCommunicatorBackend.api.v1.users.requestBody.UserUpdateRequestBody;
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
@RequestMapping(value = "/api/v1/users")
@Api(tags = "Users")
@Slf4j
public class UsersApi {

    private final UserService userService;

    public UsersApi(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(path="/{username}")
    @ApiOperation("Get certain user's data")
    public ResponseEntity<UserResponseBody> find(
            @ApiParam(value = "The username of user for which information should be returned.", required = true)
            @PathVariable String username,
            @ApiParam(value = "Optionally get avatar.")
            @RequestParam(name="getAvatar", defaultValue="true") boolean isGettingAvatar) {
        UserResponseBody userResponseBody = userService.getUser(username, isGettingAvatar);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseBody);
    }

    @PutMapping(path="/{username}")
    @ApiOperation("Update user's data")
    public ResponseEntity<UserResponseBody> update(
            @ApiParam(value = "The username of user for which information should be updated.", required = true) @PathVariable String username,
            @RequestBody UserUpdateRequestBody userUpdateRequestBody) {
        UserResponseBody userResponseBody = userService.updateUser(username, userUpdateRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseBody);
    }

    @GetMapping(path="/page")
    @ApiOperation("Find all users with pagination")
    public ResponseEntity<FoundUsersPageResponseBody> findPaginated(@ApiParam(value = "The number of page to return.", required = true)
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
                                                                    @RequestParam(name="emailSortAscending", defaultValue="true") boolean emailAsc,
                                                                    @ApiParam(value = "Optionally get avatar.")
                                                                    @RequestParam(name="getAvatar", defaultValue="true") boolean isGettingAvatar) {
        FoundUsersPageResponseBody resp = userService.findPaginated(page, size, query, fNameAsc, lNameAsc, uNameAsc, emailAsc, isGettingAvatar);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


    @GetMapping()
    @ApiOperation("Find all users")
    public ResponseEntity<List<UserResponseBody>> findUsers( @ApiParam(value = "The searching words, by which users will be found.")
                                                             @RequestParam(name="query", defaultValue="") List<String> query,
                                                             @ApiParam(value = "The sort type by first name.")
                                                             @RequestParam(name="firstNameSortAscending", defaultValue="true") boolean fNameAsc,
                                                             @ApiParam(value = "The sort type by last name.")
                                                             @RequestParam(name="lastNameSortAscending", defaultValue="true") boolean lNameAsc,
                                                             @ApiParam(value = "The sort type by username.")
                                                             @RequestParam(name="userNameSortAscending", defaultValue="true") boolean uNameAsc,
                                                             @ApiParam(value = "The sort type by email.")
                                                             @RequestParam(name="emailSortAscending", defaultValue="true") boolean emailAsc,
                                                             @ApiParam(value = "Optionally get avatar.")
                                                             @RequestParam(name="getAvatar", defaultValue="true") boolean isGettingAvatar){
        List<UserResponseBody> resp = userService.findUsers(query, fNameAsc, lNameAsc, uNameAsc, emailAsc, isGettingAvatar);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


}
