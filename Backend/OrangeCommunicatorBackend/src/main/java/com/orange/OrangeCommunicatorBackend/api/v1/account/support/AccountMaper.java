package com.orange.OrangeCommunicatorBackend.api.v1.account.support;

import com.orange.OrangeCommunicatorBackend.api.v1.account.requestBody.AccountRegisterRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountRegisterResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.account.responseBody.AccountTokenResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;

@Component
public class AccountMaper {

    public User toUser(AccountRegisterRequestBody arrb) {
        return new User(arrb.getUserName(), arrb.geteMail(), arrb.getFirstName(),
                arrb.getLastName(),arrb.getImgURL());
    }

    public AccountRegisterResponseBody toAccountRegisterResponse(User user) {
        return new AccountRegisterResponseBody(user.getUserName(), user.getEMail(),
                user.getFirstName(), user.getImgUrl(), user.getLastName());
    }

    public AccountTokenResponseBody toAccountTokenBody(String resp){

        resp = resp.substring(1);
        resp = resp.substring(0, resp.length() - 1);

        String[] fieldsVal = resp.split(",");

        int size = 8;

        String[] out = new String[8];

        for(int i = 0; i<8; i++){
            out[i] = fieldsVal[i].split(":")[1];
            out[i] = out[i].replace("\"", "");
        }

        return new AccountTokenResponseBody(out[0], Integer.parseInt(out[1]), Integer.parseInt(out[2]),
                out[3], out[4], Integer.parseInt(out[5]), out[6], out[7]);

    }
}
