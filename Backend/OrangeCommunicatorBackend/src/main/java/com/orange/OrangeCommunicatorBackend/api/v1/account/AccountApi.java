package com.orange.OrangeCommunicatorBackend.api.v1.account;


import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountLoginRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRefreshTokenRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRegisterRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountRegisterResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountTokenBody;
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
    public ResponseEntity<AccountTokenBody> login(@RequestBody AccountLoginRequestBody accountLoginRequestBody) {
        AccountTokenBody accountTokenBody = accountService.login(accountLoginRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(accountTokenBody);
    }

    @PostMapping("/{username}/refresh-token")
    @ApiOperation("Refresh token for user")
    public ResponseEntity<AccountTokenBody> refresh(@PathVariable String username, @RequestBody AccountRefreshTokenRequestBody accountRefreshTokenRequestBody) {
        AccountTokenBody accountTokenBody = accountService.refresh(accountRefreshTokenRequestBody);
        return ResponseEntity.status(HttpStatus.OK).body(accountTokenBody);
    }

    @PostMapping("/{username}/logout")
    @ApiOperation("Log out user")
    public String findAll() {
        return "/{username}/logout POST endpoint";
    }

}
