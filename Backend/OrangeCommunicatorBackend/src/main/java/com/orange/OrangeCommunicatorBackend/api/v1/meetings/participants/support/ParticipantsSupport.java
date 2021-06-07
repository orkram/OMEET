package com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.support;

import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.MeetingUserList;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.MeetingUserListRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParticipantsSupport {

    private final MeetingUserListRepository meetingUserListRepository;

    private static final String userColName = "userName";
    private static final String nameColName = "firstName";
    private static final String surnameColName = "lastName";

    private final String meetingNameCol = "name";
    private final String meetingCol = "idMeeting";

    public ParticipantsSupport(MeetingUserListRepository meetingUserListRepository) {
        this.meetingUserListRepository = meetingUserListRepository;
    }

    public Specification<User> specificationForUsers(List<String> query, List<String> usernames) {
        Specification<User> spec = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                for(String text : query){
                    predicates.add(criteriaBuilder.and(root.get(userColName).in(usernames),
                            criteriaBuilder.or(criteriaBuilder.like(root.get(userColName), "%" + text + "%"),
                                    criteriaBuilder.like(root.get(nameColName), "%" + text + "%"),
                                    criteriaBuilder.like(root.get(surnameColName), "%" + text + "%")
                           )));
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        return spec;
    }

    public Specification<Meeting> specificationForMeetings(List<String> query, List<Long> meetingsIds) {

        Specification<Meeting> spec = new Specification<Meeting>() {
            @Override
            public Predicate toPredicate(Root<Meeting> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

                for(String text : query){
                    predicates.add(criteriaBuilder.and(root.get(meetingCol).in(meetingsIds),
                            criteriaBuilder.like(root.get(meetingNameCol), "%" + text + "%")
                            ));
                }

                return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
            }
        };
        return spec;

    }

    public List<String> getUsernamesFromMeeting(Meeting meeting){
        List<MeetingUserList> usersInMeetings = meetingUserListRepository.findByMeeting(meeting);
        return  usersInMeetings.stream().map(MeetingUserList::getUser).
                map(User::getUserName).collect(Collectors.toList());
    }

    public List<Long> getMeetingsIdsFromUser(User user){
        List<MeetingUserList> meetingsOfUser = meetingUserListRepository.findByUser(user);
        return meetingsOfUser.stream().map(MeetingUserList::getMeeting).
                map(Meeting::getIdMeeting).collect(Collectors.toList());
    }
}


