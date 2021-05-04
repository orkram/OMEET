package com.orange.OrangeCommunicatorBackend.api.v1.users.support;

import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserSupport {

    private static final String userColName = "userName";
    private static final String nameColName = "firstName";
    private static final String surnameColName = "lastName";

    public Sort getSort(boolean fNameAscending,
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
        return sort;
    }

    public Specification<User> nameContains(List<String> texts){

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
