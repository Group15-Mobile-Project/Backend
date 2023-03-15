package airbnb.com.backend1.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import airbnb.com.backend1.Entity.Participant;
import airbnb.com.backend1.Entity.Response.ParticipantResponse;
import airbnb.com.backend1.Entity.Response.UserResponse;

@Component
public class ParticipantMapper {
    @Autowired
    UserMapper userMapper;

    public ParticipantResponse mapParticipantToResponse(Participant participant) {
        UserResponse user = userMapper.mapUserToResponse(participant.getUser());
        return new ParticipantResponse(participant.getId(), participant.getChat().getId(), user, participant.isRead());
    }
}
