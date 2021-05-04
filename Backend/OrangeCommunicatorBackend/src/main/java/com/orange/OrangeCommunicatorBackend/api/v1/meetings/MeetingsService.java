package com.orange.OrangeCommunicatorBackend.api.v1.meetings;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.ParticipantsService;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.participants.support.ParticipantsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.NewMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.UpdateMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingSupport;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingsMapper;
import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.MeetingUserList;
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
public class MeetingsService {

    private final MeetingsMapper meetingsMapper;
    private final MeetingRepository meetingRepository;
    private final MeetingUserListRepository meetingUserListRepository;
    private final UserRepository userRepository;
    private final MeetingSupport meetingSupport;
    private final ParticipantsService participantsService;

    public MeetingsService(MeetingsMapper meetingsMapper, MeetingRepository meetingRepository, MeetingUserListRepository meetingUserListRepository, UserRepository userRepository, MeetingSupport meetingSupport, ParticipantsService participantsService, ParticipantsMapper participantsMapper) {
        this.meetingsMapper = meetingsMapper;
        this.meetingRepository = meetingRepository;
        this.meetingUserListRepository = meetingUserListRepository;
        this.userRepository = userRepository;
        this.meetingSupport = meetingSupport;
        this.participantsService = participantsService;
    }

    public MeetingResponseBody create(NewMeetingRequestBody newMeetingRequestBody) {

        User owner = userRepository.findById(newMeetingRequestBody.getOwnerUserName()).orElseThrow();
        long id = 0;
        Meeting checkMeet = new Meeting();
        int cnt = 0;

        long leftLimit = 1L;
        long rightLimit = Long.MAX_VALUE;

        while(checkMeet != null){
            id = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
            checkMeet = meetingRepository.findById(id).orElse(null);
            cnt++;
            if(cnt == 1000){
                checkMeet = meetingRepository.findById(id).orElse(null);
                if(checkMeet != null)
                    return null;
            }
        }
        String link = "";
        Meeting meeting = meetingRepository.save(meetingsMapper.toMeeting(id, newMeetingRequestBody, link, owner));

        List<String> usernames = newMeetingRequestBody.getParticipants();
        if(!usernames.contains(owner.getUserName()))
            usernames.add(owner.getUserName());

        for(String u : usernames){
            participantsService.create(meeting.getIdMeeting(), u);
        }

        return meetingsMapper.toMeetingResponseBody(meeting);

    }

    public MeetingResponseBody get(Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow();
        return meetingsMapper.toMeetingResponseBody(meeting);
    }

    public void delete(Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow();

        List<MeetingUserList> usersInMeeting = meetingUserListRepository.findByMeeting(meeting);

        List<User> users = usersInMeeting.stream().map(MeetingUserList::getUser).collect(Collectors.toList());

        for (User u : users) {
            participantsService.delete(meeting.getIdMeeting(), u.getUserName());
        }

        meetingRepository.deleteById(meeting.getIdMeeting());
    }

    public MeetingResponseBody update(Long id, UpdateMeetingRequestBody updateMeetingRequestBody) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow();
        User newOwner = userRepository.findById(updateMeetingRequestBody.getOwnerUserName()).orElseThrow();
        meetingRepository.save(meetingsMapper.toMeeting(meeting, updateMeetingRequestBody, newOwner));
        return meetingsMapper.toMeetingResponseBody(meeting);
    }

    public List<MeetingResponseBody> getOwnersMeeting(String username, List<String> query) {
        User user = userRepository.findById(username).orElseThrow();

        if(query.size() == 0){
            query.add("");
        }

        Sort sort = meetingSupport.getSort(true);
        Specification<Meeting> spec = meetingSupport.nameContains(query, user);

        List<Meeting> meetings = meetingRepository.findAll(spec, sort);
        List<MeetingResponseBody>  responseBodies = meetings.stream().map(meetingsMapper::toMeetingResponseBody).collect(Collectors.toList());
        return responseBodies;


    }

    public MeetingsPageResponseBody getOwnersMeetingPaginated(String username, List<String> query,
                                                              int pageNr, int size, boolean mNameAsc) {

        User user = userRepository.findById(username).orElseThrow();
        Sort sort = meetingSupport.getSort(mNameAsc);
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
        List<MeetingResponseBody> meetings = page.get().map(meetingsMapper::toMeetingResponseBody).collect(Collectors.toList());
        return meetingsMapper.toMeetingsPageResponseBody(meetings, page.getTotalPages(), page.getTotalElements());
    }
}
