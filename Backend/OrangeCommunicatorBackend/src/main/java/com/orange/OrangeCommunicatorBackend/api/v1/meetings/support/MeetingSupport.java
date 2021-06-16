//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
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
import java.util.Locale;

@Component
public class MeetingSupport {

    private final String meetingNameCol = "name";
    private final String ownerCol = "user";
    private final String dateCol = "sqlTimestamp";
    private final String idCol = "idMeeting";

    public Sort getSort(boolean mNameAscending, boolean idAscending, boolean dateAscending) {
        Sort sort = null;
        if(mNameAscending) {
            sort = Sort.by(meetingNameCol).ascending();
        } else {
            sort = Sort.by(meetingNameCol).descending();
        }
        if(idAscending) {
            sort = sort.and(Sort.by(idCol).ascending());
        } else {
            sort = sort.and(Sort.by(idCol).descending());
        }
        if(dateAscending) {
            sort = sort.and(Sort.by(dateCol).ascending());
        } else {
            sort = sort.and(Sort.by(dateCol).descending());
        }
        return sort;
    }

    public Specification<Meeting> nameContains(List<String> query, User user) {

        user.setUserName(user.getUserName().toLowerCase(Locale.ROOT));

        Specification<Meeting> spec = new Specification<Meeting>() {
            @Override
            public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                for(String text : query){
                    predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get(meetingNameCol), "%" + text + "%"),
                            criteriaBuilder.equal(root.get(ownerCol), user)));
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        return spec;
    }
}
