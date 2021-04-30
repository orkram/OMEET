package com.orange.OrangeCommunicatorBackend.api.v1.meetings;

import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.NewMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.requestBody.UpdateMeetingRequestBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.responseBody.MeetingsPageResponseBody;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingSupport;
import com.orange.OrangeCommunicatorBackend.api.v1.meetings.support.MeetingsMapper;
import com.orange.OrangeCommunicatorBackend.api.v1.users.responseBody.UserResponseBody;
import com.orange.OrangeCommunicatorBackend.dbEntities.Meeting;
import com.orange.OrangeCommunicatorBackend.dbEntities.User;
import com.orange.OrangeCommunicatorBackend.dbRepositories.MeetingRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.MeetingUserListRepository;
import com.orange.OrangeCommunicatorBackend.dbRepositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MeetingsService {

    private final MeetingsMapper meetingsMapper;
    private final MeetingRepository meetingRepository;
    private final MeetingUserListRepository meetingUserListRepository;
    private final UserRepository userRepository;
    private final MeetingSupport meetingSupport;

    public MeetingsService(MeetingsMapper meetingsMapper, MeetingRepository meetingRepository, MeetingUserListRepository meetingUserListRepository, UserRepository userRepository, MeetingSupport meetingSupport) {
        this.meetingsMapper = meetingsMapper;
        this.meetingRepository = meetingRepository;
        this.meetingUserListRepository = meetingUserListRepository;
        this.userRepository = userRepository;
        this.meetingSupport = meetingSupport;
    }

    public MeetingResponseBody create(NewMeetingRequestBody newMeetingRequestBody) {

        User owner = userRepository.findById(newMeetingRequestBody.getOwnerUserName()).orElseThrow();
        long id = 0;
        Meeting checkMeet = null;
        int cnt = 0;
        while(checkMeet == null){
            id = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
            checkMeet = meetingRepository.findById(id).orElse(null);
            cnt++;
            if(cnt == 1000){
                checkMeet = meetingRepository.findById(id).orElseThrow();
            }
        }
        String link = "";
        Meeting meeting = meetingRepository.save(meetingsMapper.toMeeting(id, newMeetingRequestBody, link, owner));

        return meetingsMapper.toMeetingResponseBody(meeting);

    }

    public MeetingResponseBody get(Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow();
        return meetingsMapper.toMeetingResponseBody(meeting);
    }

    public void delete(Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow();
        meetingRepository.delete(meeting);
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
