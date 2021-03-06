//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.users.support;

import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UpdateAvatarResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.Settings;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.generalServicies.MinioService;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class UserSupport {

    private static final String userColName = "userName";
    private static final String nameColName = "firstName";
    private static final String surnameColName = "lastName";
    private static final String emailColName = "eMail";
    private static final String privColName = "isPrivate";

    private final MinioService minioService;

    public UserSupport(MinioService minioService) {
        this.minioService = minioService;
    }

    public Sort getSort(boolean fNameAscending,
                        boolean lNameAscending, boolean uNameAscending, boolean emailAsc){
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
        if(emailAsc) {
            sort = sort.and(Sort.by(emailColName).ascending());
        } else {
            sort = sort.and(Sort.by(emailColName).descending());
        }
        return sort;
    }

    public Specification<User> nameContains(List<String> texts){

        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                Join<User, Settings> j = root.join("settings");



                for(String text : texts){
                    predicates.add(criteriaBuilder.and(criteriaBuilder.or(criteriaBuilder.like(root.get(userColName), "%" + text.toLowerCase(Locale.ROOT) + "%"),
                            criteriaBuilder.or(criteriaBuilder.like(root.get(nameColName), "%" + text + "%")),
                            criteriaBuilder.or(criteriaBuilder.like(root.get(surnameColName), "%" + text + "%"))),
                                criteriaBuilder.equal(j.get(privColName), false)));
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        return spec;
    }

    public Specification<User> nameContainsInFriendsList(List<String> texts){

        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                Join<User, Settings> j = root.join("settings");



                for(String text : texts){
                    predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get(userColName), "%" + text.toLowerCase(Locale.ROOT) + "%"),
                            criteriaBuilder.or(criteriaBuilder.like(root.get(nameColName), "%" + text + "%"),
                            criteriaBuilder.or(criteriaBuilder.like(root.get(surnameColName), "%" + text + "%")))));
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        return spec;
    }

    public String processAvatar(User user, boolean isToProcess) {

        String url = null;
        if(isToProcess)
            url = minioService.avatarGetUrl(user.getUserName());
        return url;
    }


    public UpdateAvatarResponseBody toUpdateAvatarResponseBody(String userName, String url) {
        return new UpdateAvatarResponseBody(userName, url);
    }
}
