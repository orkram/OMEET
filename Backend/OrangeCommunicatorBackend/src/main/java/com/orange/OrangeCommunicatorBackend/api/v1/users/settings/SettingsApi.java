package com.orange.OrangeCommunicatorBackend.api.v1.users.settings;

import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.requestBody.UpdateSettingsRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.settings.responseBody.SettingsResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/{username}")
    @ApiOperation("Find certain user's settings")
    public ResponseEntity<SettingsResponseBody> find(@PathVariable String username) {
        SettingsResponseBody settingsResponseBody = settingsService.getSettings(username);
        return ResponseEntity.status(HttpStatus.OK).body(settingsResponseBody);
    }

    @PutMapping("/{username}")
    @ApiOperation("Update user's settings")
    public ResponseEntity<SettingsResponseBody> update(@PathVariable String username,
                                                       @RequestBody UpdateSettingsRequestBody updateSettingsRequestBody) {
        SettingsResponseBody settingsResponseBody = settingsService.updateSettings(username, updateSettingsRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(settingsResponseBody);
    }

}
