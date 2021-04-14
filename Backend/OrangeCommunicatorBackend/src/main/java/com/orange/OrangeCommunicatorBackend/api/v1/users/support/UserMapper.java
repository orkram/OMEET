package com.orange.OrangeCommunicatorBackend.api.v1.users.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.requestBody.UserUpdateRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseBody toUserResponseBody(User user){
        return new UserResponseBody(user.getUser_name(),user.getE_mail(), user.getFirst_name(),
                user.getLast_name(), user.getImg_url());
    }

    public User toUser(User user, UserUpdateRequestBody userUpdateRequestBody){
        user.setE_mail(userUpdateRequestBody.geteMail());
        user.setFirst_name(userUpdateRequestBody.getFirstName());
        user.setImg_url(userUpdateRequestBody.getImgURL());
        user.setLast_name(userUpdateRequestBody.getLastName());
        return user;
    }

}
