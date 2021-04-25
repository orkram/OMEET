package com.orange.OrangeCommunicatorBackend.api.v1.users;

import com.orange.OrangeCommunicatorBackend.api.v1.users.requestBody.UserUpdateRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserMapper;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    private static final String userColName = "userName";
    private static final String nameColName = "firstName";
    private static final String surnameColName = "lastName";


    public UserService(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }


    public UserResponseBody getUser(String userName){
        User user = userRepository.findById(userName).orElseThrow();
        return userMapper.toUserResponseBody(user);
    }

    public UserResponseBody updateUser(String userName, UserUpdateRequestBody userUpdateRequestBody){
        User user = userRepository.findById(userName).orElseThrow();
        userRepository.save(userMapper.toUser(user, userUpdateRequestBody));
        return userMapper.toUserResponseBody(user);
    }

    public FoundUsersPageResponseBody findPaginated(int pageNr, int size, List<String> query, boolean fNameAscending,
                                                    boolean lNameAscending, boolean uNameAscending){
        Sort sort;
        if(fNameAscending) {
            sort = Sort.by(nameColName).ascending();
        } else {
            sort = Sort.by(nameColName).descending();
        }
        if (lNameAscending){
            sort =  sort.and(Sort.by(surnameColName).ascending());
        } else {
            sort =  sort.and(Sort.by(surnameColName).descending());
        }
        if(uNameAscending) {
            sort = sort.and(Sort.by(userColName).ascending());
        } else {
            sort = sort.and(Sort.by(userColName).descending());
        }

        if(pageNr <= 0){
            pageNr = 1;
        }

        if(size <= 0){
            size = 1;
        }

        if(query.size() == 0){
            query.add("");
        }

        PageRequest pageRequest = PageRequest.of(pageNr - 1, size, sort);
        Specification<User> spec = nameContains(query);
        Page<User> page = userRepository.findAll(spec, pageRequest);
        //List<User> users = userRepository.findAll(spec);
        List<User> users = page.getContent();
        List<UserResponseBody>  usersResponse = users.stream().map(userMapper::toUserResponseBody).collect(Collectors.toList());
        return userMapper.toUserFoundPaged(usersResponse, page.getTotalElements(), page.getTotalPages());
    }

    public List<UserResponseBody> findUsers(List<String> query){
        Sort sort;
        sort = Sort.by(nameColName).ascending();
        sort = sort.and(Sort.by(surnameColName).ascending());
        sort = sort.and(Sort.by(userColName).ascending());

        if(query.size() == 0){
            query.add("");
        }


        Specification<User> spec = nameContains(query);
        List<User> users = userRepository.findAll(spec, sort);
        List<UserResponseBody>  usersResponse = users.stream().map(userMapper::toUserResponseBody).collect(Collectors.toList());
        return usersResponse;
    }

    private static Specification<User> nameContains(List<String> texts){

        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                for(String text : texts){
                    predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get(userColName), "%" + text + "%"),
                            criteriaBuilder.or(criteriaBuilder.like(root.get(nameColName), "%" + text + "%")),
                            criteriaBuilder.or(criteriaBuilder.like(root.get(surnameColName), "%" + text + "%"))));
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        return spec;
    }

}
