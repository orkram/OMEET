package com.orange.OrangeCommunicatorBackend.api.v1.users.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseBody toUserResponseBody(User user){
        return new UserResponseBody(user.getUser_name(),user.getE_mail(), user.getFirst_name(),
                user.getLast_name(), user.getImg_url());
    }

}
