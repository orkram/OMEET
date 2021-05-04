package com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.support.ParticipantsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.support.ParticipantsSupport;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingSupport;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.UserService;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserMapper;
import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.MeetingRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.MeetingUserListRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantsService {

    private final MeetingUserListRepository meetingUserListRepository;
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final ParticipantsMapper participantsMapper;
    private final ParticipantsSupport participantsSupport;
    private final UserMapper userMapper;
    private final MeetingsMapper meetingsMapper;
    private final MeetingSupport meetingSupport;

    public ParticipantsService(MeetingUserListRepository meetingUserListRepository, MeetingRepository meetingRepository, UserRepository userRepository, ParticipantsMapper participantsMapper, ParticipantsSupport participantsSupport, UserMapper userMapper, MeetingsMapper meetingsMapper, MeetingSupport meetingSupport) {
        this.meetingUserListRepository = meetingUserListRepository;
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.participantsMapper = participantsMapper;
        this.participantsSupport = participantsSupport;
        this.userMapper = userMapper;
        this.meetingsMapper = meetingsMapper;
        this.meetingSupport = meetingSupport;
    }

    public void create(long id, String username) {
        User user = userRepository.findById(username).orElseThrow();
        Meeting meeting = meetingRepository.findById(id).orElseThrow();

        meetingUserListRepository.save(participantsMapper.toMeetingParticipant(meeting, user));
    }

    public void delete(long id, String username) {
        User user = userRepository.findById(username).orElseThrow();
        Meeting meeting = meetingRepository.findById(id).orElseThrow();

        meetingUserListRepository.delete(participantsMapper.toMeetingParticipant(meeting, user));
    }

    public List<UserResponseBody> findParticipants(Long id, List<String> query) {

        Meeting meeting = meetingRepository.findById(id).orElseThrow();

        if(query.size() == 0){
            query.add("");
        }

        List<String> usernames =  participantsSupport.getUsernamesFromMeeting(meeting);

        Sort sort = UserService.getSort(true, true, true);
        Specification<User> spec = participantsSupport.specificationForUsers(query, usernames);

        List<User> users = userRepository.findAll(spec, sort);
        List<UserResponseBody>  responseBodies = users.stream().map(userMapper::toUserResponseBody).collect(Collectors.toList());
        return responseBodies;
    }

    public FoundUsersPageResponseBody
            findParticipantsPaginated(Long id, List<String> query, int page, int size,
                                      boolean fNameAsc, boolean lNameAsc, boolean uNameAsc) {


        Meeting meeting = meetingRepository.findById(id).orElseThrow();

        if(query.size() == 0){
            query.add("");
        }

        if(page <= 0){
            page = 1;
        }

        if(size <= 0){
            size = 1;
        }

        Sort sort = UserService.getSort(fNameAsc, lNameAsc, uNameAsc);
        List<String> usernames =  participantsSupport.getUsernamesFromMeeting(meeting);

        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Specification<User> spec = participantsSupport.specificationForUsers(query, usernames);
        Page<User> pageOfUsers = userRepository.findAll(spec, pageRequest);
        List<UserResponseBody>  usersResponse = pageOfUsers.get().map(userMapper::toUserResponseBody).collect(Collectors.toList());
        return userMapper.toUserFoundPaged(usersResponse, pageOfUsers.getTotalElements(), pageOfUsers.getTotalPages());

    }


    public List<MeetingResponseBody> findMeetings(String username, List<String> query) {

        User user = userRepository.findById(username).orElseThrow();
        if(query.size() == 0){
            query.add("");
        }

        List<Long> meetingsIds = participantsSupport.getMeetingsIdsFromUser(user);
        Sort sort = meetingSupport.getSort(true);

        Specification<Meeting> spec = participantsSupport.specificationForMeetings(query, meetingsIds);
        List<Meeting> meetings = meetingRepository.findAll(spec, sort);

        return meetings.stream().map(meetingsMapper::toMeetingResponseBody).collect(Collectors.toList());
    }

    public MeetingsPageResponseBody fingMeetingsPaginated(String username, int page, int size,
                                                          boolean mNameAsc, List<String> query) {

        User user = userRepository.findById(username).orElseThrow();

        if(query.size() == 0){
            query.add("");
        }

        if(page <= 0){
            page = 1;
        }

        if(size <= 0){
            size = 1;
        }

        List<Long> meetingsIds = participantsSupport.getMeetingsIdsFromUser(user);
        Sort sort = meetingSupport.getSort(true);

        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Specification<Meeting> spec = participantsSupport.specificationForMeetings(query, meetingsIds);

        Page<Meeting> pageOfMeetings = meetingRepository.findAll(spec, pageRequest);
        List<MeetingResponseBody> meetingResponseBodies = pageOfMeetings.get().
                map(meetingsMapper::toMeetingResponseBody).collect(Collectors.toList());
        return meetingsMapper.toMeetingsPageResponseBody(meetingResponseBodies,
                pageOfMeetings.getTotalPages(), pageOfMeetings.getTotalElements());
    }
}
