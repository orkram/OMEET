//Autorzy kodu źródłowego: Bartosz Panuś
//Kod został utworzony w ramach kursu Projekt Zespołowy
//na Politechnice Wrocławskiej
package com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.support.ParticipantsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.support.ParticipantsSupport;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingSupport;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingsExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.FoundUsersPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserSupport;
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
import java.util.Locale;
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
    private final UserSupport userSupport;

    public ParticipantsService(MeetingUserListRepository meetingUserListRepository,
                               MeetingRepository meetingRepository, UserRepository userRepository,
                               ParticipantsMapper participantsMapper, ParticipantsSupport participantsSupport,
                               UserMapper userMapper, MeetingsMapper meetingsMapper, MeetingSupport meetingSupport,
                               UserSupport userSupport) {
        this.meetingUserListRepository = meetingUserListRepository;
        this.meetingRepository = meetingRepository;
        this.userRepository = userRepository;
        this.participantsMapper = participantsMapper;
        this.participantsSupport = participantsSupport;
        this.userMapper = userMapper;
        this.meetingsMapper = meetingsMapper;
        this.meetingSupport = meetingSupport;
        this.userSupport = userSupport;
    }

    public void create(long id, String username) {
        username = username.toLowerCase(Locale.ROOT);

        User user = userRepository.findById(username)
                .orElseThrow(UserExceptionSupplier.userNotFoundException(username));
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(MeetingsExceptionSupplier.meetingNotFoundException(id));

        meetingUserListRepository.save(participantsMapper.toMeetingParticipant(meeting, user));
    }

    public void delete(long id, String username) {
        username = username.toLowerCase(Locale.ROOT);

        User user = userRepository.findById(username)
                .orElseThrow(UserExceptionSupplier.userNotFoundException(username));
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(MeetingsExceptionSupplier.meetingNotFoundException(id));

        meetingUserListRepository.delete(participantsMapper.toMeetingParticipant(meeting, user));
    }

    public List<UserResponseBody> findParticipants(Long id, List<String> query, boolean fNameAsc, boolean lNameAsc, boolean uNameAsc,
                                                   boolean emailAsc, boolean isGettingAvatar) {

        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(MeetingsExceptionSupplier.meetingNotFoundException(id));

        if(query.size() == 0){
            query.add("");
        }

        List<String> usernames =  participantsSupport.getUsernamesFromMeeting(meeting);

        Sort sort = userSupport.getSort(fNameAsc, lNameAsc, uNameAsc, emailAsc);
        Specification<User> spec = participantsSupport.specificationForUsers(query, usernames);

        List<User> users = userRepository.findAll(spec, sort);
        List<UserResponseBody>  responseBodies = users.stream()
                .map(u -> userMapper.toUserResponseBody(u, userSupport.processAvatar(u, isGettingAvatar)))
                .collect(Collectors.toList());
        return responseBodies;
    }

    public FoundUsersPageResponseBody
            findParticipantsPaginated(Long id, List<String> query, int page, int size,
                                      boolean fNameAsc, boolean lNameAsc, boolean uNameAsc, boolean emailAsc, boolean isGettingAvatar) {


        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(MeetingsExceptionSupplier.meetingNotFoundException(id));

        if(query.size() == 0){
            query.add("");
        }

        if(page <= 0){
            page = 1;
        }

        if(size <= 0){
            size = 1;
        }

        Sort sort = userSupport.getSort(fNameAsc, lNameAsc, uNameAsc, emailAsc);
        List<String> usernames =  participantsSupport.getUsernamesFromMeeting(meeting);

        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Specification<User> spec = participantsSupport.specificationForUsers(query, usernames);
        Page<User> pageOfUsers = userRepository.findAll(spec, pageRequest);
        List<UserResponseBody>  usersResponse = pageOfUsers.get()
                .map(u -> userMapper.toUserResponseBody(u, userSupport.processAvatar(u, isGettingAvatar)))
                .collect(Collectors.toList());


        return userMapper.toUserFoundPaged(usersResponse, pageOfUsers.getTotalElements(), pageOfUsers.getTotalPages());

    }


    public List<MeetingResponseBody> findMeetings(String username, List<String> query, boolean mNameAsc,
                                                  boolean idAsc, boolean dateAsc, boolean isGettingAvatar) {
        username = username.toLowerCase(Locale.ROOT);


        User user = userRepository.findById(username)
                .orElseThrow(UserExceptionSupplier.userNotFoundException(username));
        if(query.size() == 0){
            query.add("");
        }

        List<Long> meetingsIds = participantsSupport.getMeetingsIdsFromUser(user);
        Sort sort = meetingSupport.getSort(mNameAsc, idAsc, dateAsc);

        Specification<Meeting> spec = participantsSupport.specificationForMeetings(query, meetingsIds);
        List<Meeting> meetings = meetingRepository.findAll(spec, sort);

        return meetings.stream().map(meeting -> meetingsMapper.toMeetingResponseBody(meeting, isGettingAvatar))
                .collect(Collectors.toList());
    }

    public MeetingsPageResponseBody fingMeetingsPaginated(String username, int page, int size,
                                                          boolean mNameAsc, List<String> query, boolean idAsc, boolean dateAsc, boolean isGettingAvatar) {
        username = username.toLowerCase(Locale.ROOT);

        User user = userRepository.findById(username)
                .orElseThrow(UserExceptionSupplier.userNotFoundException(username));

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
        Sort sort = meetingSupport.getSort(mNameAsc, idAsc, dateAsc);

        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Specification<Meeting> spec = participantsSupport.specificationForMeetings(query, meetingsIds);

        Page<Meeting> pageOfMeetings = meetingRepository.findAll(spec, pageRequest);
        List<MeetingResponseBody> meetingResponseBodies = pageOfMeetings.get().
                map(meeting -> meetingsMapper.toMeetingResponseBody(meeting, isGettingAvatar)).collect(Collectors.toList());
        return meetingsMapper.toMeetingsPageResponseBody(meetingResponseBodies,
                pageOfMeetings.getTotalPages(), pageOfMeetings.getTotalElements());
    }
}
