package com.orange.OrangeCommunicatorBackend.api.v1.users.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.requestBody.UserUpdateRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponseBody toUserResponseBody(User user){
        return new UserResponseBody(user.getUserName(),user.getEMail(), user.getFirstName(),
                user.getLastName(), user.getImgUrl());
    }

    public User toUser(User user, UserUpdateRequestBody userUpdateRequestBody){
        user.setEMail(userUpdateRequestBody.getEMail());
        user.setFirstName(userUpdateRequestBody.getFirstName());
        user.setImgUrl(userUpdateRequestBody.getImgURL());
        user.setLastName(userUpdateRequestBody.getLastName());
        return user;
    }

    public FoundUsersPageResponseBody toUserFoundPaged(List<UserResponseBody> usersResp, long allFoundUsers, long allFoundPages){
        return new FoundUsersPageResponseBody(allFoundUsers, allFoundPages, usersResp);
    }

}
