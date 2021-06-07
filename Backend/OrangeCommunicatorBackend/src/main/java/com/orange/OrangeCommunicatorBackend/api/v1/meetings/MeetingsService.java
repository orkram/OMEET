package com.orange.OrangeCommunicatorBackend.api.v1.meetings;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.ParticipantsService;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.NewMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.UpdateMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingSupport;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingsExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserExceptionSupplier;
import com.orange.OrangeCommunicatorBackend.api.v1.users.support.UserSupport;
import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.MeetingUserList;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.MeetingRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.MeetingUserListRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class MeetingsService {

    private final MeetingsMapper meetingsMapper;
    private final MeetingRepository meetingRepository;
    private final MeetingUserListRepository meetingUserListRepository;
    private final UserRepository userRepository;
    private final UserSupport userSupport;
    private final MeetingSupport meetingSupport;
    private final ParticipantsService participantsService;

    @Value("${server.https}")
    private String https;
    @Value("${server.ip}")
    private String ip;

    public MeetingsService(MeetingsMapper meetingsMapper, MeetingRepository meetingRepository,
                           MeetingUserListRepository meetingUserListRepository, UserRepository userRepository,
                           UserSupport userSupport, MeetingSupport meetingSupport, ParticipantsService participantsService) {
        this.meetingsMapper = meetingsMapper;
        this.meetingRepository = meetingRepository;
        this.meetingUserListRepository = meetingUserListRepository;
        this.userRepository = userRepository;
        this.userSupport = userSupport;
        this.meetingSupport = meetingSupport;
        this.participantsService = participantsService;
    }

    public MeetingResponseBody create(NewMeetingRequestBody newMeetingRequestBody) {

        newMeetingRequestBody.setOwnerUserName(newMeetingRequestBody.getOwnerUserName().toLowerCase(Locale.ROOT));

        User owner = userRepository.findById(newMeetingRequestBody.getOwnerUserName())
                .orElseThrow(UserExceptionSupplier.userNotFoundException(newMeetingRequestBody.getOwnerUserName()));
        long id = 0;
        Meeting checkMeet = new Meeting();
        int cnt = 0;

        long leftLimit = 1L;
        long rightLimit = Long.MAX_VALUE;

        while(checkMeet != null){
            id = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
            checkMeet = meetingRepository.findById(id).orElse(null);
            cnt++;
            if(cnt == 10000){
                checkMeet = meetingRepository.findById(id).orElse(null);
                if(checkMeet != null)
                    throw MeetingsExceptionSupplier.creatingMeetingErrorException().get();
            }
        }
        String link = https + ip + "/" + id;
        Meeting meeting = meetingRepository.save(meetingsMapper.toMeeting(id, newMeetingRequestBody, link, owner));

        List<String> usernames = newMeetingRequestBody.getParticipants();
        if(!usernames.contains(owner.getUserName()))
            usernames.add(owner.getUserName());

        for(String u : usernames){
            participantsService.create(meeting.getIdMeeting(), u);
        }

        return meetingsMapper.toMeetingResponseBody(meeting);

    }

    public MeetingResponseBody get(Long id, boolean isGettingAvatar) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(MeetingsExceptionSupplier.meetingNotFoundException(id));
        userSupport.processAvatar(meeting.getUser(), isGettingAvatar);
        return meetingsMapper.toMeetingResponseBody(meeting);
    }

    public void delete(Long id) {
        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(MeetingsExceptionSupplier.meetingNotFoundException(id));

        List<MeetingUserList> usersInMeeting = meetingUserListRepository.findByMeeting(meeting);

        List<User> users = usersInMeeting.stream().map(MeetingUserList::getUser).collect(Collectors.toList());

        for (User u : users) {
            participantsService.delete(meeting.getIdMeeting(), u.getUserName());
        }

        meetingRepository.deleteById(meeting.getIdMeeting());
    }

    public MeetingResponseBody update(Long id, UpdateMeetingRequestBody updateMeetingRequestBody) {
        updateMeetingRequestBody.setOwnerUserName(updateMeetingRequestBody.getOwnerUserName().toLowerCase(Locale.ROOT));

        Meeting meeting = meetingRepository.findById(id)
                .orElseThrow(MeetingsExceptionSupplier.meetingNotFoundException(id));
        User newOwner = userRepository.findById(updateMeetingRequestBody.getOwnerUserName())
                .orElseThrow(UserExceptionSupplier.userNotFoundException(updateMeetingRequestBody.getOwnerUserName()));
        meetingRepository.save(meetingsMapper.toMeeting(meeting, updateMeetingRequestBody, newOwner));
        return meetingsMapper.toMeetingResponseBody(meeting);
    }

    public List<MeetingResponseBody> getOwnersMeeting(String username, List<String> query, boolean mNameAsc,
                                                      boolean idAsc, boolean dateAsc, boolean isGettingAvatar) {
        username = username.toLowerCase(Locale.ROOT);

        User user = userRepository.findById(username)
                .orElseThrow(UserExceptionSupplier.userNotFoundException(username));

        if(query.size() == 0){
            query.add("");
        }

        Sort sort = meetingSupport.getSort(mNameAsc, idAsc, dateAsc);
        Specification<Meeting> spec = meetingSupport.nameContains(query, user);

        List<Meeting> meetings = meetingRepository.findAll(spec, sort);
        List<MeetingResponseBody>  responseBodies = meetings.stream().map(m ->
            {
                userSupport.processAvatar(m.getUser(), isGettingAvatar);
                return m;
            })
                .map(meetingsMapper::toMeetingResponseBody).collect(Collectors.toList());
        return responseBodies;


    }

    public MeetingsPageResponseBody getOwnersMeetingPaginated(String username, List<String> query,
                                                              int pageNr, int size, boolean mNameAsc, boolean idAsc, boolean dateAsc, boolean isGettingAvatar) {

        username = username.toLowerCase(Locale.ROOT);

        User user = userRepository.findById(username)
                .orElseThrow(UserExceptionSupplier.userNotFoundException(username));
        Sort sort = meetingSupport.getSort(mNameAsc, idAsc, dateAsc);
        Specification<Meeting> spec = meetingSupport.nameContains(query, user);

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

        Page<Meeting> page = meetingRepository.findAll(spec, pageRequest);
        List<MeetingResponseBody> meetings = page.get().map(m ->
            {
                userSupport.processAvatar(m.getUser(), isGettingAvatar);
                return m;
            })
                .map(meetingsMapper::toMeetingResponseBody).collect(Collectors.toList());
        return meetingsMapper.toMeetingsPageResponseBody(meetings, page.getTotalPages(), page.getTotalElements());
    }
}
