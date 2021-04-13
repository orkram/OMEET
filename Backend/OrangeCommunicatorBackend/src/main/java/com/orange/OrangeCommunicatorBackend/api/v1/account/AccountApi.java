package com.orange.OrangeCommunicatorBackend.api.v1.account;


import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountLoginRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRefreshTokenRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRegisterRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountLogoutResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountRegisterResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountTokenResponseBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/v1/account")
@Api(tags = "logging, registration, nad token management")
@Slf4j
public class AccountApi {


    private final AccountService accountService;

    AccountApi(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @ApiOperation("Register new account")
    public ResponseEntity<AccountRegisterResponseBody> register(@RequestBody AccountRegisterRequestBody accountRegisterRequestBody) {
        AccountRegisterResponseBody accountRegisterResponseBody = accountService.register(accountRegisterRequestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountRegisterResponseBody);
    }

    @PostMapping("/login")
    @ApiOperation("Login user and get tokens")
    public ResponseEntity<AccountTokenResponseBody> login(@RequestBody AccountLoginRequestBody accountLoginRequestBody) {
        AccountTokenResponseBody accountTokenResponseBody = accountService.login(accountLoginRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(accountTokenResponseBody);
    }

    @PostMapping("/{username}/refresh-token")
    @ApiOperation("Refresh token for user")
    public ResponseEntity<AccountTokenResponseBody> refresh(@PathVariable String username, @RequestBody AccountRefreshTokenRequestBody accountRefreshTokenRequestBody) {
        AccountTokenResponseBody accountTokenResponseBody = accountService.refresh(accountRefreshTokenRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(accountTokenResponseBody);
    }

    @PostMapping("/{username}/logout")
    @ApiOperation("Log out user")
    public ResponseEntity<AccountLogoutResponseBody> logout(@PathVariable String username) {
        AccountLogoutResponseBody accountLogoutResponseBody = accountService.logout(username);
        return ResponseEntity.status(HttpStatus.OK).body(accountLogoutResponseBody);
    }

}
