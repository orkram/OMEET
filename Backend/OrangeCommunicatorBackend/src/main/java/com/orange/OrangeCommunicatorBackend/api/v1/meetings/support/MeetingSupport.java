package com.orange.OrangeCommunicatorBackend.api.v1.meetings.support;

import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
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
public class MeetingSupport {

    private final String meetingNameCol = "name";
    private final String ownerCol = "user";

    public Sort getSort(boolean mNameAscending) {
        Sort sort = null;
        if(mNameAscending) {
            sort = sort.and(Sort.by(meetingNameCol).ascending());
        } else {
            sort = sort.and(Sort.by(meetingNameCol).descending());
        }
        return sort;
    }

    public Specification<Meeting> nameContains(List<String> query, User user) {

        Specification<Meeting> spec = new Specification<Meeting>() {
            @Override
            public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                for(String text : query){
                    predicates.add(criteriaBuilder.or(criteriaBuilder.like(root.get(meetingNameCol), "%" + text + "%"),
                            criteriaBuilder.and(criteriaBuilder.equal(root.get(ownerCol), user.getUserName()))));
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        return spec;
    }
}
