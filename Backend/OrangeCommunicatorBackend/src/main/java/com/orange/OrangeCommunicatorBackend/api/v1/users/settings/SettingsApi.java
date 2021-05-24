package com.orange.OrangeCommunicatorBackend.api.v1.users.settings;

import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.requestBody.UpdateSettingsRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.responseBody.SettingsResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users/settings")
@Api(tags = "User's settings")
@Slf4j
public class SettingsApi {

    private final SettingsService settingsService;

    public SettingsApi(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping(path="/{username}", produces = "application/json")
    @ApiOperation("Find certain user's settings")
    public ResponseEntity<SettingsResponseBody> find(
            @ApiParam(value = "The username of user for which settings should be returned.", required = true)
            @PathVariable String username) {
        SettingsResponseBody settingsResponseBody = settingsService.getSettings(username);
        return ResponseEntity.status(HttpStatus.OK).body(settingsResponseBody);
    }

    @PutMapping(path="/{username}", produces = "application/json")
    @ApiOperation("Update user's settings")
    public ResponseEntity<SettingsResponseBody> update(
            @ApiParam(value = "The username of user for which settings should be updated.", required = true)
            @PathVariable String username,
            @RequestBody UpdateSettingsRequestBody updateSettingsRequestBody) {
        SettingsResponseBody settingsResponseBody = settingsService.updateSettings(username, updateSettingsRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(settingsResponseBody);
    }

}
