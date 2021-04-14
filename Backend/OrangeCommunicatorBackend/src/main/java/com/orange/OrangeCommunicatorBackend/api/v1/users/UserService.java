package com.orange.OrangeCommunicatorBackend.api.v1.users;

import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserMapper;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }


    public UserResponseBody getUser(String userName){
        User user = userRepository.findById(userName).orElseThrow();
        return userMapper.toUserResponseBody(user);
    }

}
