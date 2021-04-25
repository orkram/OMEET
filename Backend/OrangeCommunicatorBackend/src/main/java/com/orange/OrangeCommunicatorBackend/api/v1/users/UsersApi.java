package com.orange.OrangeCommunicatorBackend.api.v1.users;

import com.orange.OrangeCommunicatorBackend.api.v1.users.requestBody.UserUpdateRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/{username}")
    @ApiOperation("Get certain user's data")
    public ResponseEntity<UserResponseBody> find(@PathVariable String username) {
        UserResponseBody userResponseBody = userService.getUser(username);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseBody);
    }

    @PutMapping("/{username}")
    @ApiOperation("Update user's data")
    public ResponseEntity<UserResponseBody> update(@PathVariable String username, @RequestBody UserUpdateRequestBody userUpdateRequestBody) {
        UserResponseBody userResponseBody = userService.updateUser(username, userUpdateRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseBody);
    }

    @GetMapping("/page")
    @ApiOperation("Find all users with pagination")
    public ResponseEntity<FoundUsersPageResponseBody> findPaginated(@RequestParam("page") int page, @RequestParam("size")  int size,
                                                                    @RequestParam("query") List<String> query,
                                                                    @RequestParam("firstNameSortAscending") boolean fNameAsc,
                                                                    @RequestParam("lastNameSortAscending") boolean lNameAsc,
                                                                    @RequestParam("userNameSortAscending") boolean uNameAsc) {
        FoundUsersPageResponseBody resp = userService.findPaginated(page, size, query, fNameAsc, lNameAsc, uNameAsc);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


    @GetMapping()
    @ApiOperation("Find all users")
    public ResponseEntity<List<UserResponseBody>> findUsers(@RequestParam("query") List<String> query) {
        List<UserResponseBody> resp = userService.findUsers(query);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }


}
