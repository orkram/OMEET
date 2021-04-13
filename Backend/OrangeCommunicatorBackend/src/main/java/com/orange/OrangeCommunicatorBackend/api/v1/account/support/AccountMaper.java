package com.orange.OrangeCommunicatorBackend.api.v1.account.support;

import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRegisterRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountRegisterResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;

@Component
public class AccountMaper {

    public User toUser(AccountRegisterRequestBody arrb) {
        return new User(arrb.getUserName(), arrb.geteMail(), arrb.getFirstName(),
                arrb.getLastName(),arrb.getImgURL());
    }

    public AccountRegisterResponseBody toAccountRegisterResponse(User user) {
        return new AccountRegisterResponseBody(user.getUser_name(), user.getE_mail(),
                user.getFirst_name(), user.getLast_name(), user.getImg_url());
    }
}
