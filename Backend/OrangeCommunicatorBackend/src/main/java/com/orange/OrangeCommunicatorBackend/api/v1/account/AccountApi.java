//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.account;


import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountChangePasswordRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountLoginRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRefreshTokenRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRegisterRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountLogoutResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountRegisterResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountTokenResponseBody;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/account")
@Api(tags = "logging, registration, and token management")
@Slf4j
public class AccountApi {


    private final AccountService accountService;

    AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path="/register")
    @ApiOperation("Register new account")
    @ApiResponses(value = {
            @ApiResponse(code = 409, message = "Account already exists")
    })
    public ResponseEntity<AccountRegisterResponseBody> register(@RequestBody AccountRegisterRequestBody accountRegisterRequestBody) {
        AccountRegisterResponseBody accountRegisterResponseBody = accountService.register(accountRegisterRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountRegisterResponseBody);
    }

    @PostMapping(path="/login")
    @ApiOperation("Login user and get tokens")
    public ResponseEntity<AccountTokenResponseBody> login(@RequestBody AccountLoginRequestBody accountLoginRequestBody) {
        AccountTokenResponseBody accountTokenResponseBody = accountService.login(accountLoginRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(accountTokenResponseBody);
    }

    @PostMapping(path="/{username}/refresh-token")
    @ApiOperation("Refresh token for user")
    public ResponseEntity<AccountTokenResponseBody>
                refresh(@ApiParam(value = "The username of user which token needs to be refreshed.", required = true)
                        @PathVariable String username,
                        @RequestBody AccountRefreshTokenRequestBody accountRefreshTokenRequestBody) {
        AccountTokenResponseBody accountTokenResponseBody = accountService.refresh(accountRefreshTokenRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(accountTokenResponseBody);
    }

    @PostMapping(path="/{username}/logout")
    @ApiOperation("Log out user")
    public ResponseEntity<AccountLogoutResponseBody> logout(
            @ApiParam(value = "The username of user who wants to be logged out.", required = true) @PathVariable String username) {
        AccountLogoutResponseBody accountLogoutResponseBody = accountService.logout(username);
        return ResponseEntity.status(HttpStatus.OK).body(accountLogoutResponseBody);
    }

    @PostMapping(path="/{username}/change-password")
    @ApiOperation("Change password of user")
    public ResponseEntity<Void> logout(@ApiParam(value = "The username of user who wants to change password.", required = true)
                @PathVariable String username,
                @RequestBody AccountChangePasswordRequestBody accountChangePasswordRequestBody) {
                accountService.changePassword(accountChangePasswordRequestBody, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
